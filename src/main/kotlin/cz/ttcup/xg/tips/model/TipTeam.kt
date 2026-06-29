package cz.ttcup.xg.tips.model

import kotlinx.serialization.Serializable

@Serializable
data class TipTeam(
    val name: String,
    val logo: String,
    val forecastScore: Double,
)