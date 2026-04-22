package cz.ttcup.player.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players
import kotlinx.coroutines.flow.firstOrNull
import org.bson.BsonDocument

suspend fun getPlayer(uid: String? = null, stisId: String? = null): Player? {
    withDatabase {
        return database.players
            .find(
                listOfNotNull(
                    uid?.let { Player::uid eq it },
                    stisId?.let { Player::stisId eq it },
                ).takeIf {
                    it.isNotEmpty()
                }?.let {
                    Filters.and(it)
                } ?: BsonDocument(),
            )
            .firstOrNull()
    }
}