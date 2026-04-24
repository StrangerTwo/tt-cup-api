package cz.ttcup.bet.service

import com.mongodb.client.model.Filters
import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.schema.bets
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import kotlinx.coroutines.flow.firstOrNull
import org.bson.BsonDocument
import org.bson.conversions.Bson
import org.bson.types.ObjectId

suspend fun getBet(
    matchId: ObjectId? = null,
    sort: Bson? = null,
): Bet? {
    withDatabase {
        return database.bets
            .find(
                listOfNotNull(
                    matchId?.let { Bet::matchId eq it },
                ).takeIf {
                    it.isNotEmpty()
                }?.let {
                    Filters.and(it)
                } ?: BsonDocument(),
            )
            .apply {
                sort?.let { this.sort(it) }
            }
            .firstOrNull()
    }
}