package cz.ttcup.util.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedQueryResult<T>(
    val results: List<T>,
    val pageInfo: PageInfo,
) {
}

infix operator fun <T> PaginatedQueryResult<T>.plus(other: PaginatedQueryResult<T>) =
    PaginatedQueryResult<T>(
        this.results + other.results,
        PageInfo(
            this.pageInfo.page,
            this.pageInfo.size + other.pageInfo.size,
            this.pageInfo.totalElements + other.pageInfo.totalElements,
            this.pageInfo.totalPages + other.pageInfo.totalPages
        )
    )

