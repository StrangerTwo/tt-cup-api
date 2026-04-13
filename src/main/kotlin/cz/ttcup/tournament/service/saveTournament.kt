package cz.ttcup.tournament.service

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.matches
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.schema.tournaments

suspend fun saveTournament(tournament: Tournament, matches: List<Match>) {
    withDatabase {
        val tournamentId = database.tournaments
            .findOneAndUpdate(
                Filters.and(
                    Tournament::date eq tournament.date,
                    Tournament::place eq tournament.place,
                ),
                Tournament::place set tournament.place,
                FindOneAndUpdateOptions()
                    .upsert(true)
                    .returnDocument(ReturnDocument.AFTER)
            )!!.objectId!!

        database.matches
            .deleteMany(
                Match::tournamentId eq tournamentId,
            )
        database.matches
            .insertMany(
                matches.map { it.copy(tournamentId = tournamentId) }
            )
    }
}