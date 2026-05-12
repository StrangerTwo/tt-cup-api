package cz.ttcup.match.model

import cz.ttcup.util.ShortNamedEnum
import cz.ttcup.util.serializer.shortNameEnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable(with = CustomFilterType.Companion.Serializer::class)
enum class CustomFilterType(
    override val shortName: String,
) : ShortNamedEnum {
    STAR_PLAYER("*"),
    ELO_DIFFERENCE("ELO"),
    RANK_DIFFERENCE("RANK"),
    BILANCE("BIL"),
    LAST_MATCH_STRONG_LOST("L_WON"),
    ;

    companion object {
        object Serializer : KSerializer<CustomFilterType> by shortNameEnumSerializer()
    }
}