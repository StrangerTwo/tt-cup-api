package cz.ttcup.match.model

import cz.ttcup.bet.model.Bet
import kotlinx.serialization.Serializable

@Serializable
data class TopMatch(
    val match: Match,
    val bet: Bet? = null,
)