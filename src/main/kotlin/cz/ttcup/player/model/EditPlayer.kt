package cz.ttcup.player.model

import kotlinx.serialization.Serializable

@Serializable
data class EditPlayer(
    val location: Location? = null,
    val note: String? = null,
    val stisUrl: String? = null,
)