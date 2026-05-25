package cz.ttcup.match.model

import cz.ttcup.player.model.Player
import cz.ttcup.summary.model.Summary
import cz.ttcup.util.model.PageInfo
import kotlinx.serialization.Serializable

@Serializable
data class TopMatches(
    val results: List<TopMatch>,
    val pageInfo: PageInfo,
    val players: List<Player>,
    val summaries: List<Summary>,
)