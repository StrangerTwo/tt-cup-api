package cz.ttcup.util.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedQueryResult<T>(
    val results: List<T>,
    val pageInfo: PageInfo,
) {
    fun <R> map(fn: (item: T) -> R): PaginatedQueryResult<R> {
        return PaginatedQueryResult(results.map(fn), pageInfo)
    }

    constructor(results: List<T>) : this(results, PageInfo(0, results.size, results.size.toLong(), 1))
}

infix operator fun <T> PaginatedQueryResult<T>.plus(other: PaginatedQueryResult<T>) =
    PaginatedQueryResult(
        this.results + other.results,
        PageInfo(
            this.pageInfo.page,
            this.pageInfo.size + other.pageInfo.size,
            this.pageInfo.totalElements + other.pageInfo.totalElements,
            this.pageInfo.totalPages + other.pageInfo.totalPages
        )
    )

