package cz.ttcup.behavior.model

import cz.ttcup.util.ShortNamedEnum
import cz.ttcup.util.serializer.shortNameEnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable(with = PlayerBehaviorType.Companion.Serializer::class)
enum class PlayerBehaviorType(
    override val shortName: String,
    val instance: PlayerBehavior,
) : ShortNamedEnum {
    ZERO_WINS("0", ZeroWinsBehavior),
    ONE_WINS("1", OneWinsBehavior),
    TWO_WINS("2", TwoWinsBehavior),
    THREE_WINS("3", ThreeWinsBehavior),
    ;

    companion object {
        object Serializer : KSerializer<PlayerBehaviorType> by shortNameEnumSerializer()
    }
}