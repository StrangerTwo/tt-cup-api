package cz.ttcup.tournament.model

import cz.ttcup.player.model.Location
import cz.ttcup.util.model.PageInfoRequest
import cz.ttcup.util.serializer.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class GetTournamentsRequest(
    val location: Location? = null,
    val done: Boolean? = null,
    @Serializable(with = LocalTimeSerializer::class)
    val from: LocalTime? = null,
    @Serializable(with = LocalTimeSerializer::class)
    val to: LocalTime? = null,
    val pageInfo: PageInfoRequest
)