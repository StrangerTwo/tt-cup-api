package cz.ttcup.tournament.service

import com.mongodb.client.model.Aggregates
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.fieldName
import cz.ttcup.match.model.Match
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.model.TournamentFull
import cz.ttcup.tournament.schema.tournaments
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

suspend fun getTournamentFull(tournamentId: String): TournamentFull? {
    withDatabase {
        return database.tournaments
            .aggregate<TournamentFull>(
                listOf(
                    Aggregates.match(
                        Tournament::objectId eq ObjectId(tournamentId)
                    ),
                    Aggregates.lookup(
                        "matches",
                        Tournament::objectId.fieldName,
                        Match::tournamentId.fieldName,
                        "matches"
                    )
                )
            )
            .firstOrNull()
    }
}