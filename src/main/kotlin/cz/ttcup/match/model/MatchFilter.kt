package cz.ttcup.match.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface MatchFilter {
    val type: MatchFilterType
}

@Serializable
@SerialName("*")
object StarPlayerFilter : MatchFilter {
    override val type = MatchFilterType.STAR_PLAYER
}

@Serializable
@SerialName("ELO")
data class EloDifferenceFilter(val value: Int) : MatchFilter {
    override val type = MatchFilterType.ELO_DIFFERENCE
}

@Serializable
@SerialName("RANK")
data class RankDifferenceFilter(val value: Int) : MatchFilter {
    override val type = MatchFilterType.RANK_DIFFERENCE
}

@Serializable
@SerialName("BIL")
data class BilanceFilter(val value: Int) : MatchFilter {
    override val type = MatchFilterType.BILANCE
}

@Serializable
@SerialName("L_WON")
object LastMatchStrongLostFilter : MatchFilter {
    override val type = MatchFilterType.LAST_MATCH_STRONG_LOST
}