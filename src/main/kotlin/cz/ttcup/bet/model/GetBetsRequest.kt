package cz.ttcup.bet.model

import cz.ttcup.player.model.Location
import cz.ttcup.util.model.PageInfoRequest
import kotlinx.serialization.Serializable

@Serializable
data class GetBetsRequest(
    val location: Location? = null,
    val tournamentId: String? = null,
    val matchId: String? = null,
    val playerUids: List<String>? = null,
    val type: BetType? = null,
    val pageInfo: PageInfoRequest
)