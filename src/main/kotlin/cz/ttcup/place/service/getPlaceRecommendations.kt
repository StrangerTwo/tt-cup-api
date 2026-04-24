package cz.ttcup.place.service

import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Field
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.contains
import cz.ttcup.database.util.desc
import cz.ttcup.database.util.exists
import cz.ttcup.database.util.fieldName
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.MATCHES
import cz.ttcup.place.model.GetPlaceRecommendationsRequest
import cz.ttcup.place.model.Place
import cz.ttcup.place.model.TournamentMatches
import cz.ttcup.place.schema.PLACES
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.model.TournamentFull
import cz.ttcup.tournament.schema.TOURNAMENTS
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated
import org.bson.Document

suspend fun getPlaceRecommendations(request: GetPlaceRecommendationsRequest): PaginatedQueryResult<TournamentMatches> {
    withDatabase {
        return database.getCollection<TournamentMatches>(TOURNAMENTS)
            .queryPaginated(
                request.pageInfo,
                filter = request.search?.let { Tournament::place contains it },
                aggregate = listOf(
                    Aggregates.sort(Tournament::date.desc()),
                    Aggregates.group(
                        "$${Tournament::place.fieldName}",
                        Accumulators.first(TournamentMatches::tournament.fieldName, "\$\$ROOT")
                    ),
                    Aggregates.lookup(
                        PLACES,
                        "tournament.${Tournament::place.fieldName}",
                        Place::uid.fieldName,
                        TournamentFull::place.fieldName
                    ),
                    Aggregates.addFields(
                        Field(
                            TournamentFull::place.fieldName,
                            Document("\$first", "$${TournamentFull::place.fieldName}")
                        )
                    ),
                    Aggregates.match(TournamentFull::place.exists(false)),
                    Aggregates.lookup(
                        MATCHES,
                        "tournament.${Tournament::objectId.fieldName}",
                        Match::tournamentId.fieldName,
                        TournamentMatches::matches.fieldName,
                    )
                ),
                logDebug = true,
            )
    }
}

