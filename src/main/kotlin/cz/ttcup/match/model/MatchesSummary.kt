package cz.ttcup.match.model

import kotlinx.serialization.Serializable

@Serializable
data class MatchesSummary(
    val matchesWon: Int,
    val matchesLost: Int,
    val longestWinStreak: Int,
    val longestLoseStreak: Int,
    val currentWinStreak: Int,
    val currentLoseStreak: Int,
)