package cz.ttcup.player.service

import com.mongodb.client.result.UpdateResult
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players

suspend fun setPlayerFavourite(player: Player, favourite: Boolean): UpdateResult {
    withDatabase {
        return database.players
            .updateOne(
                Player::uid eq player.uid,
                Player::favourite set favourite
            )
    }
}