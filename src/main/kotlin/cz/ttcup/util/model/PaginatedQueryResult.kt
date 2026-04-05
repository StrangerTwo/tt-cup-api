package cz.ttcup.util.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedQueryResult<T>(
    val results: List<T>,
    val pageInfo: PageInfo,
) {
}