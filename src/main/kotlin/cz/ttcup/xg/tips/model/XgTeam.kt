package cz.ttcup.xg.tips.model

import kotlinx.serialization.Serializable

@Serializable
data class XgTeam(
    val name: String,
    val logoUrl: String,
    val countryId: String?
)