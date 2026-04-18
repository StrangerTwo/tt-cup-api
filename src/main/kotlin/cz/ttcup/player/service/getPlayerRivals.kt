package cz.ttcup.player.service

import cz.ttcup.database.util.eq
import cz.ttcup.match.model.Match
import cz.ttcup.match.model.RivalMatchesSummary
import cz.ttcup.match.service.calculateMatchesSummary
import cz.ttcup.match.service.getAllMatches
import cz.ttcup.player.model.Player

suspend fun getPlayerRivals(player: Player): List<RivalMatchesSummary> {
    val matches = getAllMatches(Match::playerUids eq player.uid)
    return matches
        .groupBy { it.opponent(player.uid) }
        .map { (rivalUid, matches) ->
            calculateMatchesSummary(matches, player).let {
                RivalMatchesSummary(
                    rivalUid = rivalUid,
                    it.matchesWon,
                    it.matchesLost,
                    it.longestWinStreak,
                    it.longestLoseStreak,
                    it.currentWinStreak,
                    it.currentLoseStreak
                )
            }
        }
}