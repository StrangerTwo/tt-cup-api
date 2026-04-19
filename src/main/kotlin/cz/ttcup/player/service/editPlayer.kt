package cz.ttcup.player.service

import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.player.model.EditPlayer
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players

suspend fun editPlayer(player: Player, request: EditPlayer): UpdateResult {
    withDatabase {
        return database.players
            .updateOne(
                Player::uid eq player.uid,
                Updates.combine(
                    request.location?.let { Player::location set it },
                    request.note?.let { Player::note set it },
                )
            )
    }
}