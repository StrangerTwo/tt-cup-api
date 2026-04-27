package cz.ttcup.tournament.model

import cz.ttcup.player.model.Player
import cz.ttcup.util.model.PageInfo
import kotlinx.serialization.Serializable

@Serializable
data class Tournaments(
    val results: List<TournamentFull>,
    val pageInfo: PageInfo,
    val players: List<Player>,
)