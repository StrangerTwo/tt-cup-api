package cz.ttcup.xg.tips.model

import kotlinx.serialization.Serializable

@Serializable
data class RemovedTipIds(
    val tipIds: List<String>,
)