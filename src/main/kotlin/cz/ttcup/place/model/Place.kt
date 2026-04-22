package cz.ttcup.place.model

import cz.ttcup.player.model.Location
import cz.ttcup.util.serializer.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Place(
    val uid: String,
    val location: Location?,
    @Serializable(with = LocalDateSerializer::class)
    val lastUpdate: LocalDate,
    val disabled: Boolean? = null,
)