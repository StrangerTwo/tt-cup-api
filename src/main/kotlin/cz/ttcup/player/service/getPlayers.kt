package cz.ttcup.player.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.`in`
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players
import kotlinx.coroutines.flow.toList

suspend fun getPlayers(): List<Player> {
    withDatabase {
        return database.players
            .find()
            .toList()
    }
}

suspend fun getPlayers(uids: List<String>): List<Player> {
    withDatabase {
        return database.players
            .find(Player::uid `in` uids)
            .toList()
    }
}