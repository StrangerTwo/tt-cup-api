package cz.ttcup.tournament.service

import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Updates
import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.schema.bets
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
                Updates.combine(
                    Tournament::place set tournament.place,
                    Tournament::done set tournament.done,
                ),
                FindOneAndUpdateOptions()
                    .upsert(true)
                    .returnDocument(ReturnDocument.AFTER)
            )!!.objectId

        val bets = database.bets
            .find(Bet::tournamentId eq tournamentId)
            .toList()
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
                        ),
                    )

                val bet = bets.find { it.matchId == oldMatch.objectId }
                if (bet != null) {
                    database.bets.updateOne(
                        Bet::objectId eq bet.objectId,
                        Bet::win set bet.isWin(match)
                    )
                }
            } else {
                database.matches
                    .insertOne(match.copy(tournamentId = tournamentId))
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