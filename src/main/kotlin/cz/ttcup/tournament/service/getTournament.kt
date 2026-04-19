package cz.ttcup.tournament.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.schema.tournaments
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId

suspend fun getTournament(tournamentId: String): Tournament? {
    withDatabase {
        return database.tournaments
            .find(Tournament::objectId eq ObjectId(tournamentId))
            .firstOrNull()
    }
}