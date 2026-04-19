package cz.ttcup.player.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val uid: String,
    var name: String? = null,
    var ranking: Int? = null,
    val elo: Int? = null,

    val favourite: Boolean? = null,
    val location: Location? = null,
    val note: String? = null,
    val stisId: String? = null,

    val recrawl: Boolean? = null,
)

