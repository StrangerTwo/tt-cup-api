package cz.ttcup.player.service

import cz.ttcup.database.util.eq
import cz.ttcup.match.model.Match
import cz.ttcup.match.model.MatchesSummary
import cz.ttcup.match.service.getAllMatches
import cz.ttcup.player.model.Player

suspend fun getPlayerMatchesSummary(player: Player): MatchesSummary {
    val matches = getAllMatches(Match::playerUids eq player.uid)
    val wins = matches.map { it.isWin(player.uid) }.filterNotNull()

    var longestWinStreak = 0
    var longestLoseStreak = 0

    var currentWinStreak = 0
    var currentLoseStreak = 0

    for (win in wins) {
        if (win) {
            currentWinStreak++
            currentLoseStreak = 0
        } else {
            currentLoseStreak++
            currentWinStreak = 0
        }

        longestWinStreak = maxOf(longestWinStreak, currentWinStreak)
        longestLoseStreak = maxOf(longestLoseStreak, currentLoseStreak)
    }

    return MatchesSummary(
        matchesWon = wins.count { it },
        matchesLost = wins.count { !it },
        longestWinStreak = longestWinStreak,
        longestLoseStreak = longestLoseStreak,
        currentWinStreak = currentWinStreak,
        currentLoseStreak = currentLoseStreak,
    )
}

