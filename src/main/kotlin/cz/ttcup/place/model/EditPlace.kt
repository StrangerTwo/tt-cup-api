package cz.ttcup.place.model

import cz.ttcup.player.model.Location
import kotlinx.serialization.Serializable

@Serializable
data class EditPlace(
    val location: Location? = null,
)