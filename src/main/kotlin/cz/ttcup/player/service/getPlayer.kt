package cz.ttcup.player.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players
import kotlinx.coroutines.flow.firstOrNull

suspend fun getPlayer(uid: String): Player? {
    withDatabase {
        return database.players
            .find(Player::uid eq uid)
            .firstOrNull()
    }
}