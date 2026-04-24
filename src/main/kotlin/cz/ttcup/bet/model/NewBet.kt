package cz.ttcup.bet.model

import kotlinx.serialization.Serializable

@Serializable
data class NewBet(
    val type: BetType
)