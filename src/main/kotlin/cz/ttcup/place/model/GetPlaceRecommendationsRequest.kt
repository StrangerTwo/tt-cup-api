package cz.ttcup.place.model

import cz.ttcup.util.model.PageInfoRequest
import kotlinx.serialization.Serializable

@Serializable
data class GetPlaceRecommendationsRequest(
    val search: String? = null,
    val pageInfo: PageInfoRequest
)