package cz.ttcup.match.model

import cz.ttcup.util.model.PageInfoRequest
import kotlinx.serialization.Serializable

@Serializable
data class GetMatchesRequest(
    val playerUids: List<String>? = null,
    val pageInfo: PageInfoRequest
)