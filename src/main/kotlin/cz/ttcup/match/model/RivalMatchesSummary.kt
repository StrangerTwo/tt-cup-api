package cz.ttcup.match.model

import cz.ttcup.player.model.Player
import kotlinx.serialization.Serializable

@Serializable
data class RivalMatchesSummary(
    val rivalUid: String,
    val rival: Player? = null,
    val matchesSummary: MatchesSummary,
)