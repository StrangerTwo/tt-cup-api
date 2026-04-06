package cz.ttcup.player.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players
import kotlinx.coroutines.flow.toList

suspend fun getAllPlayers(): List<Player> {
    withDatabase {
        return database.players
            .find()
            .toList()
    }
}