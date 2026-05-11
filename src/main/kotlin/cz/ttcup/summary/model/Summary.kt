package cz.ttcup.summary.model

import com.mongodb.client.model.Updates
import cz.ttcup.database.util.set
import cz.ttcup.database.util.unset
import cz.ttcup.match.model.MatchesSummary
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson

@Serializable
data class Summary(
    val playerUid: String,
    val rivalUid: String? = null,

    val matchesWon: Int,
    val matchesLost: Int,
    val longestWinStreak: Int,
    val longestLoseStreak: Int,
    val currentWinStreak: Int,
    val currentLoseStreak: Int,
    val winRate: Double? = null,
) {
    fun toUpdateBson(): Bson =
        Updates.combine(
            Summary::matchesWon set matchesWon,
            Summary::matchesLost set matchesLost,
            Summary::longestWinStreak set longestWinStreak,
            Summary::longestLoseStreak set longestLoseStreak,
            Summary::currentWinStreak set currentWinStreak,
            Summary::currentLoseStreak set currentLoseStreak,
            if (winRate == null) Summary::winRate.unset() else Summary::winRate set winRate,
        )

    constructor(playerUid: String, rivalUid: String?, summary: MatchesSummary) : this(
        playerUid = playerUid,
        rivalUid = rivalUid,
        matchesWon = summary.matchesWon,
        matchesLost = summary.matchesLost,
        longestWinStreak = summary.longestWinStreak,
        longestLoseStreak = summary.longestLoseStreak,
        currentWinStreak = summary.currentWinStreak,
        currentLoseStreak = summary.currentLoseStreak,
        winRate = if ((summary.matchesWon + summary.matchesLost) == 0) null else summary.matchesWon.toDouble() / (summary.matchesWon + summary.matchesLost).toDouble(),
    )
}