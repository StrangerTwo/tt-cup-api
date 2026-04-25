package cz.ttcup.tournament.service

import com.mongodb.client.model.*
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
            .bulkWrite(
                matches
                    .map {
                        UpdateOneModel(
                            Filters.and(
                                Match::tournamentId eq tournamentId,
                                Match::time eq it.time,
                            ),
                            Updates.combine(
                                Match::sets set it.sets,
                                Match::points set it.points,
                                Match::playerUids set it.playerUids,
                                Match::time set it.time,
                            ),
                            UpdateOptions().upsert(true)
                        )
                    }
            )
    }
}