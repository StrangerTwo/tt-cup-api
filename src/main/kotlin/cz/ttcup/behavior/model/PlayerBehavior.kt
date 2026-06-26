package cz.ttcup.behavior.model

import cz.ttcup.util.service.toInt
import kotlinx.serialization.Serializable

@Serializable
data class PlayerBehavior(
    val type: PlayerBehaviorType,
    val items: List<PlayerBehaviorItem?>
)

@Serializable
data class PlayerBehaviorItem(
    val winRate: Double? = null,
    val wins: Int,
    val losses: Int,
) {
    constructor() : this(null, 0, 0)
    constructor(wins: Int, losses: Int) : this(
        if (wins + losses == 0) null else wins.toDouble() / (wins + losses).toDouble(),
        wins, losses
    )

    constructor(isWin: Boolean?) : this((isWin == true).toInt(), (isWin == false).toInt())


    operator fun plus(other: PlayerBehaviorItem): PlayerBehaviorItem = PlayerBehaviorItem(
        wins = wins + other.wins,
        losses = losses + other.losses,
    )
}