package cz.ttcup.tournament.service

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.schema.BETS
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.*
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.MATCHES
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.PLACES
import cz.ttcup.tournament.model.GetTournamentsRequest
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.model.TournamentFull
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
                preFilterAggregate = listOf(
                    Aggregates.project(
                        Projections.computed(TournamentFull::tournament.fieldName, "\$\$ROOT")
                    ),
                    *lookupFirst(
                        PLACES,
                        Tournament::place.fieldName,
                        Place::uid.fieldName,
                        TournamentFull::place.fieldName,
                    ),
                    lookup(
                        MATCHES,
                        "tournament.${Tournament::objectId.fieldName}",
                        Match::tournamentId.fieldName,
                        TournamentFull::matches.fieldName,
                        listOf(
                            Aggregates.sort(Match::time.asc())
                        )
                    ),
                    lookup(
                        BETS,
                        "tournament.${Tournament::objectId.fieldName}",
                        Bet::tournamentId.fieldName,
                        TournamentFull::bets.fieldName,
                    )
                ),
                filter = listOfNotNull(
                    request.location?.let { "place.${Place::location.fieldName}" eq it },
                ).takeIf {
                    it.isNotEmpty()
                }?.let {
                    Filters.and(
                        *it.toTypedArray()
                    )
                },
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