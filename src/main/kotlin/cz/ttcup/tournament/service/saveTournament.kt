package cz.ttcup.tournament.service

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.result.UpdateResult
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.schema.tournaments

suspend fun saveTournament(tournament: Tournament): UpdateResult {
    withDatabase {
        return database.tournaments
            .updateOne(
                Filters.and(
                    Tournament::date eq tournament.date,
                    Tournament::place eq tournament.place,
                ),
                Tournament::matches set tournament.matches,
                UpdateOptions().upsert(true)
            )
    }
}