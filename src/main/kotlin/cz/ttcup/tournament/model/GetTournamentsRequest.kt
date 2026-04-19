package cz.ttcup.tournament.model

import cz.ttcup.util.model.PageInfoRequest
import kotlinx.serialization.Serializable

@Serializable
data class GetTournamentsRequest(
//    val location: Location? = null,
    val pageInfo: PageInfoRequest
)