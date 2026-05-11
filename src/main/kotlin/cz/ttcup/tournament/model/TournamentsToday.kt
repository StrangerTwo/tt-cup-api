package cz.ttcup.tournament.model

import cz.ttcup.player.model.Player
import cz.ttcup.summary.model.Summary
import cz.ttcup.util.model.PageInfo
import kotlinx.serialization.Serializable

@Serializable
data class TournamentsToday(
    val results: List<TournamentToday>,
    val pageInfo: PageInfo,
    val players: List<Player>,
    val summaries: List<Summary>,
)