package cz.ttcup.player.service

import cz.ttcup.database.util.all
import cz.ttcup.match.model.Match
import cz.ttcup.match.model.MatchesSummary
import cz.ttcup.match.service.calculateMatchesSummary
import cz.ttcup.match.service.getAllMatches

suspend fun getRivalrySummary(playerUid: String, rivalUid: String): MatchesSummary {
    val matches = getAllMatches(
        Match::playerUids all listOf(playerUid, rivalUid)
    )
    return calculateMatchesSummary(matches, playerUid)
}