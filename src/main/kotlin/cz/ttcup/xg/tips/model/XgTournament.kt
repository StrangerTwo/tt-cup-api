package cz.ttcup.xg.tips.model

import kotlinx.serialization.Serializable

@Serializable
data class XgTournament(
    val id: String,
    val name: String,
    val slug: String,
    val logoUrl: String,
    val country: XgCountry?
)