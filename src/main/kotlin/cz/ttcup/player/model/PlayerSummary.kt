package cz.ttcup.player.model

import cz.ttcup.summary.model.Summary
import kotlinx.serialization.Serializable

@Serializable
data class PlayerSummary(
    val player: Player,
    val summary: Summary
)