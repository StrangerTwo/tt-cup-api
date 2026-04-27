package cz.ttcup.bet.model

import cz.ttcup.match.model.Match
import cz.ttcup.player.model.Location
import cz.ttcup.util.ShortNamedEnum
import cz.ttcup.util.serializer.ObjectIdSerializer
import cz.ttcup.util.serializer.shortNameEnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Bet(
    val location: Location? = null,
    @Serializable(with = ObjectIdSerializer::class)
    val tournamentId: ObjectId,
    @Serializable(with = ObjectIdSerializer::class)
    val matchId: ObjectId,
    val playerUids: List<String>,

    val type: BetType,
    val win: Boolean? = null,

    @SerialName("_id")
    @Serializable(with = ObjectIdSerializer::class)
    val objectId: ObjectId,
) {
    fun isWin(match: Match): Boolean? {
        return isWin(type, match)
    }

    companion object {
        fun isWin(type: BetType, match: Match): Boolean? {
            return when (type) {
                BetType.WIN_1 -> match.sets?.get(0)?.let { it == 3 }
                BetType.WIN_2 -> match.sets?.get(1)?.let { it == 3 }
                BetType.NO_BET -> null
                else -> null
            }
        }
    }
}

@Serializable(with = BetType.Companion.Serializer::class)
enum class BetType(
    override val shortName: String,
) : ShortNamedEnum {
    NO_BET("N"),
    WIN_1("W1"),
    WIN_2("W2"),
    ;

    companion object {
        object Serializer : KSerializer<BetType> by shortNameEnumSerializer()
    }
}