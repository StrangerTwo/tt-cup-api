package cz.ttcup.xg.tips.model

import kotlinx.serialization.Serializable

@Serializable
data class XgTeams(
    val h: XgTeam,
    val a: XgTeam,
)