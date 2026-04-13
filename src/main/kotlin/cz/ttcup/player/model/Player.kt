package cz.ttcup.player.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val uid: String,
    var name: String? = null,
    var elo: Int? = null,
    var recrawl: Boolean? = null
)