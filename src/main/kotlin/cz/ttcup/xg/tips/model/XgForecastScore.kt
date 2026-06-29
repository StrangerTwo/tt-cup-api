package cz.ttcup.xg.tips.model

import kotlinx.serialization.Serializable

@Serializable
data class XgForecastScore(
    val h: Double,
    val a: Double,
)