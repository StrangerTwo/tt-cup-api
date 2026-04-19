package cz.ttcup.util.service

import cz.ttcup.util.model.PageInfo
import cz.ttcup.util.model.PaginatedQueryResult

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