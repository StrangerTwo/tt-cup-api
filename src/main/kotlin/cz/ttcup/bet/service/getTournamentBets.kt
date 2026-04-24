package cz.ttcup.bet.service

import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.schema.bets
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.tournament.model.Tournament
import kotlinx.coroutines.flow.toList

suspend fun getTournamentBets(tournament: Tournament): List<Bet> {
    withDatabase {
        return database.bets
            .find(Bet::tournamentId eq tournament.objectId)
            .toList()
    }
}