package cz.ttcup.place.model

import cz.ttcup.player.model.Location
import cz.ttcup.util.serializer.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Place(
    val uid: String,
    val location: Location?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val lastUpdate: LocalDateTime,
    val disabled: Boolean? = null,
)