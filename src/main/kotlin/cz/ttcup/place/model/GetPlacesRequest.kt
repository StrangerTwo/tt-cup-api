package cz.ttcup.place.model

import cz.ttcup.player.model.Location
import cz.ttcup.util.model.PageInfoRequest
import kotlinx.serialization.Serializable

@Serializable
data class GetPlacesRequest(
    val search: String? = null,
    val location: Location? = null,
    val pageInfo: PageInfoRequest
)