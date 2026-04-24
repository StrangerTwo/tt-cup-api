package cz.ttcup.bet.service

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.schema.bets
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set

suspend fun upsertBet(bet: Bet): UpdateResult {
    withDatabase {
        return database.bets
            .updateOne(
                Filters.and(
                    Bet::matchId eq bet.matchId,
                ),
                Updates.combine(
                    Bet::location set bet.location,
                    Bet::tournamentId set bet.tournamentId,
                    Bet::playerUids set bet.playerUids,
                    Bet::type set bet.type,
                ),
                UpdateOptions().upsert(true)
            )
    }
}