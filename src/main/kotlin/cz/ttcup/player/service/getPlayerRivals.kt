package cz.ttcup.player.service

import cz.ttcup.match.model.RivalMatchesSummary
import cz.ttcup.match.service.calculateMatchesSummary
import cz.ttcup.match.service.getRivalMatches
import cz.ttcup.player.model.GetPlayerRivalsRequest
import cz.ttcup.player.model.Player
import cz.ttcup.util.model.PaginatedQueryResult

suspend fun getPlayerRivals(
    player: Player,
    request: GetPlayerRivalsRequest
): PaginatedQueryResult<RivalMatchesSummary> {
    val rivalMatchesPage = getRivalMatches(request, player)

    return rivalMatchesPage.map { (rivalUid, rival, matches) ->
        RivalMatchesSummary(
            rivalUid.toString(),
            rival,
            calculateMatchesSummary(matches, player)
        )
    }
}

