package cz.ttcup.tournament.service

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Field
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.schema.BETS
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
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.model.TournamentToday
import cz.ttcup.tournament.schema.TOURNAMENTS
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated
import org.bson.Document
import java.time.LocalDate

suspend fun getTournamentsToday(request: GetTournamentsRequest): PaginatedQueryResult<TournamentToday> {
    withDatabase {
        return database.getCollection<TournamentToday>(PLACES)
            .queryPaginated(
                request.pageInfo,
                preFilterAggregate = listOfNotNull(
                    Aggregates.match(
                        Place::disabled ne true,
                    ),
                    Aggregates.project(
                        Projections.computed(TournamentToday::place.fieldName, "\$\$ROOT")
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
                                "localField" to "place.${Place::uid.fieldName}",
                                "foreignField" to Tournament::place.fieldName,
                                "as" to TournamentToday::tournament.fieldName,
                                "pipeline" to listOf(
                                    Aggregates.match(Tournament::date gte LocalDate.now()),
                                )
                            )
                        )
                    ),
                    Aggregates.addFields(
                        Field(
                            TournamentToday::tournament.fieldName,
                            Document("\$first", "$${TournamentToday::tournament.fieldName}")
                        )
                    ),
                    Aggregates.lookup(
                        MATCHES,
                        "tournament.${Tournament::objectId.fieldName}",
                        Match::tournamentId.fieldName,
                        TournamentToday::matches.fieldName,
                    ),
                    Aggregates.lookup(
                        BETS,
                        "tournament.${Tournament::objectId.fieldName}",
                        Bet::tournamentId.fieldName,
                        TournamentToday::bets.fieldName,
                    ),
                ),
                logDebug = true,
            )
    }

}