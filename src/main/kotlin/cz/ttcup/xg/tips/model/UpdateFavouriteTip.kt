package cz.ttcup.xg.tips.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateFavouriteTip(
    val favourite: Boolean,
    val tickets: List<Int>? = null
)