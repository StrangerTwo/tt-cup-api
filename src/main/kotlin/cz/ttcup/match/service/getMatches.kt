package cz.ttcup.match.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.match.model.GetMatchesRequest
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.matches
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated

suspend fun getMatches(request: GetMatchesRequest): PaginatedQueryResult<Match> {
    withDatabase {
        return database.matches
            .queryPaginated(
                request.pageInfo,
                request.playerUid?.let { Match::playerUids eq request.playerUid },
            )
    }
}