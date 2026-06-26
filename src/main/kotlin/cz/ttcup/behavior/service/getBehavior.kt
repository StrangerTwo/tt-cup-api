package cz.ttcup.behavior.service

import com.mongodb.client.model.Filters
import cz.ttcup.behavior.model.Behavior
import cz.ttcup.behavior.schema.behaviors
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.player.model.Player
import kotlinx.coroutines.flow.firstOrNull
import org.bson.BsonDocument
import org.bson.conversions.Bson

suspend fun getBehavior(
    player: Player? = null,
    sort: Bson? = null,
): Behavior? {
    withDatabase {
        return database.behaviors
            .find(
                listOfNotNull(
                    player?.let { Behavior::playerUid eq it.uid },
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