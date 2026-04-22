package cz.ttcup.match.model

import cz.ttcup.player.model.Player

data class RivalMatches(
    val _id: String,

    val rival: Player? = null,

    val matches: List<Match>,
)