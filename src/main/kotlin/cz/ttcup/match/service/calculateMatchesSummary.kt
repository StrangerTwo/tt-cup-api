package cz.ttcup.match.service

import cz.ttcup.match.model.Match
import cz.ttcup.match.model.MatchesSummary
import cz.ttcup.player.model.Player

fun calculateMatchesSummary(
    matches: List<Match>,
    player: Player
): MatchesSummary {
    val wins = matches.mapNotNull { it.isWin(player.uid) }

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