package cz.ttcup.player.service

import cz.ttcup.database.util.eq
import cz.ttcup.match.model.Match
import cz.ttcup.match.model.MatchesSummary
import cz.ttcup.match.service.calculateMatchesSummary
import cz.ttcup.match.service.getAllMatches
import cz.ttcup.player.model.Player

suspend fun getPlayerMatchesSummary(player: Player): MatchesSummary {
    val matches = getAllMatches(Match::playerUids eq player.uid)
    return calculateMatchesSummary(matches, player)
}
