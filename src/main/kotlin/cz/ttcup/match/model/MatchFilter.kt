package cz.ttcup.match.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface MatchFilter {
}

@Serializable
@SerialName("*")
object StarPlayerFilter : MatchFilter {
}

@Serializable
@SerialName("ELO")
data class EloDifferenceFilter(val value: Int) : MatchFilter {
}

@Serializable
@SerialName("RANK")
data class RankDifferenceFilter(val value: Int) : MatchFilter {
}

@Serializable
@SerialName("BIL")
data class BilanceFilter(val value: Int) : MatchFilter {
}

@Serializable
@SerialName("L_WON")
object LastMatchStrongLostFilter : MatchFilter {
}