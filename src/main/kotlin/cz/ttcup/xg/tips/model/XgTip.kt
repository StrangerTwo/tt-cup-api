package cz.ttcup.xg.tips.model

import cz.ttcup.util.serializer.LocalDateTimeSerializer
import cz.ttcup.util.serializer.ObjectIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import java.time.LocalDateTime

@Serializable
data class XgTip(
    val id: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val datetime: LocalDateTime,
    val recommendation: String,
    val recommendationChance: Double,

    val url: String,

    val tournament: TipTournament,
    val homeTeam: TipTeam,
    val awayTeam: TipTeam,

    @SerialName("_id")
    @Serializable(with = ObjectIdSerializer::class)
    val objectId: ObjectId? = null,
) {
    constructor(forecast: XgForecast) : this(
        id = forecast.id,
        datetime = forecast.game.datetime,
        recommendationChance = forecast.maxCf,
        recommendation = forecast.text,
        url = "https://xgscore.io/${forecast.game.tournament.slug}/${forecast.game.slug}",
        tournament = TipTournament(
            logo = forecast.game.tournament.logoUrl,
            name = forecast.game.tournament.name,
            country = forecast.game.tournament.country?.name
        ),
        homeTeam = TipTeam(
            name = forecast.game.teams.h.name,
            logo = forecast.game.teams.h.logoUrl,
            forecastScore = forecast.game.forecastScore.h
        ),
        awayTeam = TipTeam(
            name = forecast.game.teams.a.name,
            logo = forecast.game.teams.a.logoUrl,
            forecastScore = forecast.game.forecastScore.a
        ),
    )
}