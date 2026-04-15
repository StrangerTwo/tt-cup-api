package cz.ttcup.player.model

import cz.ttcup.util.model.PageInfoRequest
import kotlinx.serialization.Serializable

@Serializable
data class GetPlayersRequest(
    val search: String? = null,
    val pageInfo: PageInfoRequest
)