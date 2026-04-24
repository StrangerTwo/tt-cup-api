package cz.ttcup.bet.service

import com.mongodb.client.model.Filters
import com.mongodb.client.result.DeleteResult
import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.schema.bets
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import org.bson.types.ObjectId

suspend fun deleteBet(
    matchId: ObjectId,
): DeleteResult {
    withDatabase {
        return database.bets
            .deleteOne(
                Filters.and(
                    Bet::matchId eq matchId,
                )
            )
    }
}