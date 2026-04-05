package cz.ttcup.util.model

import kotlinx.serialization.Serializable
import kotlin.math.ceil

@Serializable
data class PageInfo(
    val page: Int,
    val size: Int = 20,
    val totalElements: Long,
    val totalPages: Int,
    val sort: List<String>? = null,
) {
    constructor(pageInfo: PageInfoRequest, totalElements: Long) : this(
        pageInfo.page,
        pageInfo.size,
        totalElements,
        ceil(totalElements.toDouble() / pageInfo.size).toInt(),
        pageInfo.sort
    )
}
