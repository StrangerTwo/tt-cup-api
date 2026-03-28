package cz.ttcup.player.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val uid: String,
    val name: String? = null,
    val elo: Int? = null,
)