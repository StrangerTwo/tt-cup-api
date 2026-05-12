package cz.ttcup.match.model

import cz.ttcup.util.model.PageInfoRequest
import kotlinx.serialization.Serializable

@Serializable
data class GetTopMatchesRequest(
    val filters: List<CustomFilter>,
    val pageInfo: PageInfoRequest
)