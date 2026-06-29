package cz.ttcup.xg.tips.model

import kotlinx.serialization.Serializable

@Serializable
data class TipTournament(
    val logo: String,
    val name: String,
    val country: String?,
)