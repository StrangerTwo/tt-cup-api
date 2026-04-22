package cz.ttcup.player.service

import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.database.util.unset
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players

suspend fun updatePlayer(
    uid: String,
    name: String? = null,
    ranking: Int? = null,
    elo: Int? = null,
): UpdateResult {
    withDatabase {
        return database.players
            .updateOne(
                Player::uid eq uid,
                listOfNotNull(
                    name?.let { Player::name set it },
                    ranking?.let { Player::ranking set it },
                    elo?.let { Player::elo set it },
                ).takeIf {
                    it.isNotEmpty()
                }?.let {
                    Updates.combine(
                        *it.toTypedArray(),
                        Player::recrawl.unset()
                    )
                } ?: throw IllegalArgumentException("No fields to update"),
            )
    }
}