package cz.ttcup.tournament.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.ne
import cz.ttcup.tournament.model.GetTournamentsRequest
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.schema.tournaments
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated
import kotlinx.coroutines.flow.toList
import org.bson.BsonDocument
import org.bson.conversions.Bson
import java.time.LocalDate

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

suspend fun getTournaments(
    place: String? = null,
    done: Boolean? = null,
    date: LocalDate? = null,
    sort: Bson? = null,
): List<Tournament> {
    withDatabase {
        return database.tournaments
            .find(
                listOfNotNull(
                    place?.let { Tournament::place eq it },
                    done?.let { if (done) Tournament::done eq true else Tournament::done ne true },
                    date?.let { Tournament::date eq it },
                ).takeIf {
                    it.isNotEmpty()
                }?.let {
                    Filters.and(it)
                } ?: BsonDocument(),
            )
            .apply {
                sort?.let { this.sort(it) }
            }
            .toList()
    }
}