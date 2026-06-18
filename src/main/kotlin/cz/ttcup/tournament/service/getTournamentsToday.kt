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
import cz.ttcup.player.service.getPlayers
import cz.ttcup.summary.service.getSummaries
import cz.ttcup.tournament.model.GetTournamentsTodayRequest
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.model.TournamentToday
import cz.ttcup.tournament.model.TournamentsToday
import cz.ttcup.tournament.schema.TOURNAMENTS
import cz.ttcup.util.service.queryPaginated
import java.time.LocalDate

suspend fun getTournamentsToday(request: GetTournamentsTodayRequest): TournamentsToday {
    withDatabase {
        val (results, pageInfo) = database.getCollection<TournamentToday>(PLACES)
            .queryPaginated(
                request.pageInfo,
                preFilterAggregate = listOfNotNull(
                    Aggregates.match(
                        Place::enabled eq true,
                    ),
                    Aggregates.project(
                        Projections.computed(TournamentToday::place.fieldName, "\$\$ROOT")
                    ),
                    *lookupFirst(
                        TOURNAMENTS,
                        "place.${Place::uid.fieldName}",
                        Tournament::place.fieldName,
                        TournamentToday::tournament.fieldName,
                        listOf(
                            Aggregates.match(Tournament::date gte LocalDate.now()),
                        )
                    ),
                ),
                filter = listOfNotNull(
                    request.location?.let { "place.${Place::location.fieldName}" eq it },
                    request.from?.let { "place.${Place::startTime.fieldName}" gte it },
                    request.to?.let { "place.${Place::startTime.fieldName}" lte it },
                    request.done?.let {
                        if (it) "tournament.${Tournament::done.fieldName}" eq true
                        else "tournament.${Tournament::done.fieldName}" ne true
                    },
                ).takeIf {
                    it.isNotEmpty()
                }?.let {
                    Filters.and(
                        *it.toTypedArray()
                    )
                },
                resultFacetPipeline = listOf(
                    lookup(
                        MATCHES,
                        TournamentToday::matches.fieldName,
                        "tournament.${Tournament::objectId.fieldName}",
                        Match::tournamentId.fieldName,
                        listOf(
                            Aggregates.sort(Match::time.asc())
                        )
                    ),
                    lookup(
                        BETS,
                        TournamentToday::bets.fieldName,
                        "tournament.${Tournament::objectId.fieldName}",
                        Bet::tournamentId.fieldName,
                    ),
                )
            )

        val players = results
            .flatMap { it.matches }
            .flatMap { it.playerUids }
            .distinct()
            .takeIf { it.isNotEmpty() }
            ?.let { getPlayers(it) } ?: listOf()
        val summaries = results
            .flatMap { it.matches }
            .map { it.playerUids[0] to it.playerUids[1] }
            .distinct()
            .takeIf { it.isNotEmpty() }
            ?.let { getSummaries(it) } ?: listOf()

        return TournamentsToday(
            results,
            pageInfo,
            players,
            summaries
        )
    }
}

