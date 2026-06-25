package cz.ttcup.behavior.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.logs.service.logger
import cz.ttcup.player.schema.players
import kotlinx.coroutines.flow.toList

suspend fun recalculatePlayerBehaviors() {
    withDatabase {
        val players = database.players.find().toList()

        players.forEachIndexed { index, player ->
            logger.info("$index / ${players.size}: ${player.uid}")

            recalculatePlayerBehavior(player.uid)
        }
    }
}