package cz.ttcup.place.service

import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.desc
import cz.ttcup.database.util.fieldName
import cz.ttcup.match.model.Match
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.PLACES
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.model.TournamentFull
import cz.ttcup.tournament.schema.tournaments
import kotlinx.coroutines.flow.toList

suspend fun getPlaceRecommendations(): List<TournamentFull> {
    withDatabase {
        return database.tournaments
            .aggregate<TournamentFull>(
                listOf(
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
                ).let {
                    println(it.map { it.toBsonDocument().toJson() })
                    it
                }
            )
            .toList()
    }
}
