package cz.ttcup.behavior.model

import cz.ttcup.match.model.Match
import cz.ttcup.util.ShortNamedEnum
import cz.ttcup.util.serializer.shortNameEnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable(with = PlayerBehaviorType.Companion.Serializer::class)
enum class PlayerBehaviorType(
    override val shortName: String,
    val calculateTournamentBehavior: (playerUid: String, matches: List<Match>) -> List<PlayerBehaviorItem>,
) : ShortNamedEnum {
    ZERO_WINS("0", { playerUid, matches -> atWinCountBehavior(matches, playerUid, 0) }),
    ONE_WINS("1", { playerUid, matches -> atWinCountBehavior(matches, playerUid, 1) }),
    TWO_WINS("2", { playerUid, matches -> atWinCountBehavior(matches, playerUid, 2) }),
    THREE_WINS("3", { playerUid, matches -> atWinCountBehavior(matches, playerUid, 3) }),
    ;

    companion object {
        object Serializer : KSerializer<PlayerBehaviorType> by shortNameEnumSerializer()
    }
}


private fun atWinCountBehavior(
    matches: List<Match>,
    playerUid: String,
    winCount: Int
): List<PlayerBehaviorItem> {
    val playerMatches = matches.filter { it.playerUids.contains(playerUid) }

    return sequence {
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
    }.map { PlayerBehaviorItem(it) }.toList()
}
