package cz.ttcup.player.model

import kotlinx.serialization.Serializable

@Serializable
data class NewPlayer(
    val uid: String,
    val location: Location? = null,
)