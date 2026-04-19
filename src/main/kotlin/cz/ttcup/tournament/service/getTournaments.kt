package cz.ttcup.tournament.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.tournament.model.GetTournamentsRequest
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.schema.tournaments
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated

suspend fun getTournaments(request: GetTournamentsRequest): PaginatedQueryResult<Tournament> {
    withDatabase {
        return database.tournaments
            .queryPaginated(
                request.pageInfo,
//                listOfNotNull(
//                    request.location?.let { Tournament::location eq it },
//                ).takeIf {
//                    it.isNotEmpty()
//                }?.let {
//                    Filters.and(it)
//                }
            )
    }
}