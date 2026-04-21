package cz.ttcup.player.service

import cz.ttcup.database.util.all
import cz.ttcup.match.model.Match
import cz.ttcup.match.model.RivalMatchesSummary
import cz.ttcup.match.service.calculateMatchesSummary
import cz.ttcup.match.service.getAllMatches
import cz.ttcup.player.model.Player

suspend fun getPlayerRival(player: Player, rival: Player): RivalMatchesSummary {
    val matches = getAllMatches(
        Match::playerUids all listOf(player.uid, rival.uid)
    )
    return RivalMatchesSummary(rival.uid, rival, calculateMatchesSummary(matches, player))
}