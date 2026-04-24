package cz.ttcup.match.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.matches
import kotlinx.coroutines.flow.firstOrNull
import org.bson.BsonDocument
import org.bson.conversions.Bson
import org.bson.types.ObjectId

suspend fun getMatch(
    matchId: String? = null,
    sort: Bson? = null,
): Match? {
    withDatabase {
        return database.matches
            .find(
                listOfNotNull(
                    matchId?.let { Match::objectId eq ObjectId(it) },
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