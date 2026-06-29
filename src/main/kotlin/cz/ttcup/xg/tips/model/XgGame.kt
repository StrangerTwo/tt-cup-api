package cz.ttcup.xg.tips.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class XgGame(
    val slug: String,
    @Contextual
    val datetime: LocalDateTime,
    val popularity: Double,
    val preview: Boolean,
    val tournament: XgTournament,
    val forecastScore: XgForecastScore,
    val teams: XgTeams
)