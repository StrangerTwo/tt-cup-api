package cz.ttcup.tournament.service

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Updates
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.`in`
import cz.ttcup.database.util.set
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.matches
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.schema.tournaments
import kotlinx.coroutines.flow.toList

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
            )!!.objectId

        val oldMatches = database.matches
            .find(Match::tournamentId eq tournamentId)
            .toList()
            .toMutableList()

        for (match in matches) {
            val oldMatch = oldMatches.find { it.time == match.time }
            if (oldMatch != null) {
                oldMatches.remove(oldMatch)
                database.matches
                    .updateOne(
                        Match::objectId eq oldMatch.objectId,
                        Updates.combine(
                            Match::sets set match.sets,
                            Match::points set match.points,
                            Match::playerUids set match.playerUids,
                            Match::time set match.time,
                        ),
                    )
            } else {
                database.matches
                    .insertOne(match)
            }
        }
        if (oldMatches.isNotEmpty()) {
            database.matches
                .deleteMany(
                    Match::objectId `in` oldMatches.map { it.objectId },
                )
        }
    }
}