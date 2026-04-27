package cz.ttcup.bet.model

import kotlinx.serialization.Serializable

@Serializable
data class BetSummary(
    val betsWon: Int,
    val betsLost: Int,
    val betsUnresolved: Int,
)