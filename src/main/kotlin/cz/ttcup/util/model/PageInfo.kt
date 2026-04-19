package cz.ttcup.util.model

import cz.ttcup.util.service.IntPaginatedQueryResult
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
    constructor(pageInfo: PageInfoRequest, result: IntPaginatedQueryResult) : this(
        pageInfo.page,
        result.results.size,
        result.totalElements ?: 0,
        ceil((result.totalElements ?: 0).toDouble() / pageInfo.size).toInt(),
        pageInfo.sort
    )
}
