package cz.ttcup.match.model

import kotlinx.serialization.Serializable

@Serializable
data class SetTopMatchConfigRequest(
    val filters: List<MatchFilter>,
)