package cz.ttcup.behavior.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.logs.service.logger
import cz.ttcup.player.schema.players
import kotlinx.coroutines.flow.toList
import kotlin.system.measureTimeMillis

suspend fun recalculatePlayerBehaviors() {
    withDatabase {
        val players = database.players.find().toList()

        players.forEachIndexed { index, player ->
            val duration = measureTimeMillis {
                recalculatePlayerBehavior(player.uid)
            }

            logger.info("${index + 1} / ${players.size}: ${player.uid} took $duration ms")
        }
    }
}