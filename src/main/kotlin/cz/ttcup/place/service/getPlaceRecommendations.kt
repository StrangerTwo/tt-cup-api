package cz.ttcup.place.service

import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.contains
import cz.ttcup.database.util.desc
import cz.ttcup.database.util.fieldName
import cz.ttcup.match.model.Match
import cz.ttcup.place.model.GetPlaceRecommendationsRequest
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.PLACES
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.model.TournamentFull
import cz.ttcup.tournament.schema.TOURNAMENTS
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated

suspend fun getPlaceRecommendations(request: GetPlaceRecommendationsRequest): PaginatedQueryResult<TournamentFull> {
    withDatabase {
        return database.getCollection<TournamentFull>(TOURNAMENTS)
            .queryPaginated(
                request.pageInfo,
                filter = request.search?.let { Tournament::place contains it },
                aggregate = listOf(
                    Aggregates.sort(Tournament::date.desc()),
                    Aggregates.group(
                        "$${Tournament::place.fieldName}",
                        Accumulators.first("tournament", "\$\$ROOT")
                    ),
                    Aggregates.lookup(
                        PLACES,
                        "tournament.${Tournament::place.fieldName}",
                        Place::uid.fieldName,
                        "place"
                    ),
                    Aggregates.match(
                        Filters.exists("place.0", false)
                    ),
                    Aggregates.replaceRoot("\$tournament"),
                    Aggregates.lookup(
                        "matches",
                        Tournament::objectId.fieldName,
                        Match::tournamentId.fieldName,
                        "matches"
                    )
                )
            )
    }
}
