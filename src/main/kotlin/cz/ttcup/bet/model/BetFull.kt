package cz.ttcup.bet.model

import cz.ttcup.match.model.Match
import kotlinx.serialization.Serializable

@Serializable
data class BetFull(
    val bet: Bet,
    val match: Match,
)