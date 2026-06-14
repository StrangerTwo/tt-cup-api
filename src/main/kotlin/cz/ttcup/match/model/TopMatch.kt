package cz.ttcup.match.model

import cz.ttcup.bet.model.Bet
import cz.ttcup.player.model.Player
import cz.ttcup.summary.model.Summary
import kotlinx.serialization.Serializable

@Serializable
data class TopMatch(
    val match: Match,
    val bet: Bet? = null,
    val players: List<Player>? = null,
    val summary: Summary? = null,
)