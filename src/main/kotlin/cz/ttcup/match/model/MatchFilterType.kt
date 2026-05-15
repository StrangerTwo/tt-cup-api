package cz.ttcup.match.model

import cz.ttcup.util.ShortNamedEnum
import cz.ttcup.util.serializer.shortNameEnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable(with = MatchFilterType.Companion.Serializer::class)
enum class MatchFilterType(
    override val shortName: String,
) : ShortNamedEnum {
    STAR_PLAYER("*"),
    ELO_DIFFERENCE("ELO"),
    RANK_DIFFERENCE("RANK"),
    BILANCE("BIL"),
    LAST_MATCH_STRONG_LOST("L_WON"),
    ;

    companion object {
        object Serializer : KSerializer<MatchFilterType> by shortNameEnumSerializer()
    }
}