package cz.ttcup.xg.tips.model

import kotlinx.serialization.Serializable

@Serializable
data class XgForecast(
    val id: String,
    val oddsCategory: String,
    val oddsLabel: String,
    val startCf: Double,
    val maxCf: Double,
    val chance: Double,
    val type: String,
    val gameId: String,
    val game: XgGame,
    val valueBet: Double,
    val text: String,
)