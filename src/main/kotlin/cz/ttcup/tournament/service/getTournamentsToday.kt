package cz.ttcup.tournament.service

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.fieldName
import cz.ttcup.database.util.gte
import cz.ttcup.database.util.ne
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.MATCHES
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.PLACES
import cz.ttcup.tournament.model.GetTournamentsRequest
import cz.ttcup.tournament.model.PlaceTournamentMatches
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.schema.TOURNAMENTS
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated
import org.bson.Document
import java.time.LocalDate

suspend fun getTournamentsToday(request: GetTournamentsRequest): PaginatedQueryResult<PlaceTournamentMatches> {
    withDatabase {
        return database.getCollection<PlaceTournamentMatches>(PLACES)
            .queryPaginated(
                request.pageInfo,
                preFilterAggregate = listOfNotNull(
                    Aggregates.match(
                        Place::disabled ne true,
                    ),
                    Aggregates.project(
                        Projections.computed(PlaceTournamentMatches::place.fieldName, "\$\$ROOT")
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
                postSortAggregate = listOf(
                    Document(
                        "\$lookup",
                        Document(
                            mapOf(
                                "from" to TOURNAMENTS,
                                "localField" to "place.uid",
                                "foreignField" to Tournament::place,
                                "as" to "tournaments",
                                "pipeline" to listOf(
                                    Aggregates.match(Tournament::date gte LocalDate.now()),
                                )
                            )
                        )
                    ),
                    Aggregates.project(
                        Projections.fields(
                            Projections.include("place"),
                            Projections.computed(
                                PlaceTournamentMatches::tournament.fieldName,
                                Document("\$first", "tournaments")
                            ),
                        )
                    ),
                    Aggregates.lookup(
                        MATCHES,
                        "tournament._id",
                        Match::tournamentId.fieldName,
                        PlaceTournamentMatches::matches.fieldName,
                    ),
                ),
                logDebug = true,
            )
    }

}