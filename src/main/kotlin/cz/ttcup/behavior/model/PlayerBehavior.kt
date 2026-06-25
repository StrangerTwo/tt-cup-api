package cz.ttcup.behavior.model

import cz.ttcup.match.model.Match
import cz.ttcup.util.service.indexOfNth
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PlayerBehavior {
    fun calculateTournamentBehavior(playerUid: String, matches: List<Match>): List<Boolean?>
}

@Serializable
@SerialName("0")
object ZeroWinsBehavior : PlayerBehavior {
    override fun calculateTournamentBehavior(playerUid: String, matches: List<Match>): List<Boolean?> {
        return atWinCountBehavior(matches, playerUid, 0)
    }
}

@Serializable
@SerialName("1")
object OneWinsBehavior : PlayerBehavior {
    override fun calculateTournamentBehavior(playerUid: String, matches: List<Match>): List<Boolean?> {
        return atWinCountBehavior(matches, playerUid, 1)
    }
}

@Serializable
@SerialName("2")
object TwoWinsBehavior : PlayerBehavior {
    override fun calculateTournamentBehavior(playerUid: String, matches: List<Match>): List<Boolean?> {
        return atWinCountBehavior(matches, playerUid, 2)
    }
}

@Serializable
@SerialName("3")
object ThreeWinsBehavior : PlayerBehavior {
    override fun calculateTournamentBehavior(playerUid: String, matches: List<Match>): List<Boolean?> {
        return atWinCountBehavior(matches, playerUid, 3)
    }
}

private fun atWinCountBehavior(
    matches: List<Match>,
    playerUid: String,
    winCount: Int,
): List<Boolean?> {
    val playerMatches = matches.filter { it.playerUids.contains(playerUid) }
    val playerWon = playerMatches.map { it.isWin(playerUid) }

    val winIndex = playerWon.indexOfNth(n = winCount - 1) { it == true }
    val nextWinIndex = playerWon.indexOfNth(n = winCount) { it == true }

    return List(playerWon.size) {
        if (it <= winIndex) return@List null
        if (it > nextWinIndex) return@List null
        it == nextWinIndex
    }
}