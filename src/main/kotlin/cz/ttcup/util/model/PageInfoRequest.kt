package cz.ttcup.util.model

import kotlinx.serialization.Serializable

@Serializable
data class PageInfoRequest(
    val page: Int,
    val size: Int = 20,
    val sort: List<String>? = null,
)