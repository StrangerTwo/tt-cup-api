package cz.ttcup.summary.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.exists
import cz.ttcup.player.model.Player
import cz.ttcup.summary.model.Summary
import cz.ttcup.summary.schema.summaries
import kotlinx.coroutines.flow.firstOrNull
import org.bson.BsonDocument
import org.bson.conversions.Bson

suspend fun getSummary(
    player: Player? = null,
    sort: Bson? = null,
    playerUid: String? = null,
    rivalUid: String? = null,
): Summary? {
    withDatabase {
        return database.summaries
            .find(
                listOfNotNull(
                    player?.let { Summary::playerUid eq it.uid },
                    player?.let { Summary::rivalUid.exists(false) },
                    playerUid?.let { Summary::playerUid eq it },
                    rivalUid?.let { Summary::rivalUid eq it },
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