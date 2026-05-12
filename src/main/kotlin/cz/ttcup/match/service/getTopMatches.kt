package cz.ttcup.match.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.match.model.GetTopMatchesRequest
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.matches
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated

suspend fun getTopMatches(request: GetTopMatchesRequest): PaginatedQueryResult<Match> {
    withDatabase {
        return database.matches
            .queryPaginated(
                request.pageInfo,
            )
    }
}