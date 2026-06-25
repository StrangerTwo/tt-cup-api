package cz.ttcup.behavior.model

import cz.ttcup.match.model.Match
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PlayerBehavior {
    fun calculateTournamentBehavior(playerUid: String, matches: List<Match>): Sequence<Boolean?>
}

@Serializable
@SerialName("0")
object ZeroWinsBehavior : PlayerBehavior {
    override fun calculateTournamentBehavior(playerUid: String, matches: List<Match>) =
        atWinCountBehavior(matches, playerUid, 0)
}

@Serializable
@SerialName("1")
object OneWinsBehavior : PlayerBehavior {
    override fun calculateTournamentBehavior(playerUid: String, matches: List<Match>) =
        atWinCountBehavior(matches, playerUid, 1)
}

@Serializable
@SerialName("2")
object TwoWinsBehavior : PlayerBehavior {
    override fun calculateTournamentBehavior(playerUid: String, matches: List<Match>) =
        atWinCountBehavior(matches, playerUid, 2)
}

@Serializable
@SerialName("3")
object ThreeWinsBehavior : PlayerBehavior {
    override fun calculateTournamentBehavior(playerUid: String, matches: List<Match>) =
        atWinCountBehavior(matches, playerUid, 3)
}

private fun atWinCountBehavior(
    matches: List<Match>,
    playerUid: String,
    winCount: Int
): Sequence<Boolean?> = sequence {
    val playerMatches = matches.filter { it.playerUids.contains(playerUid) }

    var currentWinCount = 0;
    playerMatches.forEach {
        val isWin = it.isWin(playerUid)
        if (currentWinCount != winCount) {
            yield(null)
        } else {
            yield(isWin)
        }
        if (isWin == true) currentWinCount++
    }
}
