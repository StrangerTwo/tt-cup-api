package cz.ttcup.place.model

import cz.ttcup.player.model.Location
import cz.ttcup.util.serializer.LocalDateTimeSerializer
import cz.ttcup.util.serializer.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.LocalTime

@Serializable
data class Place(
    val uid: String,
    val name: String,
    @Serializable(with = LocalTimeSerializer::class)
    val startTime: LocalTime,

    val location: Location? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val lastUpdate: LocalDateTime? = null,
    val enabled: Boolean? = null,
)