package cz.ttcup.match.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface CustomFilter {
    val type: CustomFilterType
}

@Serializable
@SerialName("*")
object StarPlayerFilter : CustomFilter {
    override val type = CustomFilterType.STAR_PLAYER
}

@Serializable
@SerialName("ELO")
data class EloDifferenceFilter(val value: Int) : CustomFilter {
    override val type = CustomFilterType.ELO_DIFFERENCE
}