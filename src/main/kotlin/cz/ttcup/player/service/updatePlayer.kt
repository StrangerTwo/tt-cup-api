package cz.ttcup.player.service

import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.database.util.unset
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players

suspend fun updatePlayer(player: Player): UpdateResult {
    withDatabase {
        return database.players
            .updateOne(
                Player::uid eq player.uid,
                Updates.combine(
                    Player::name set player.name,
                    Player::ranking set player.ranking,
                    Player::recrawl.unset()
                )
            )
    }
}