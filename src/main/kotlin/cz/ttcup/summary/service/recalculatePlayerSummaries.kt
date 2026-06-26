package cz.ttcup.summary.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.logs.service.logger
import cz.ttcup.player.schema.players
import kotlinx.coroutines.flow.toList
import kotlin.system.measureTimeMillis

suspend fun recalculatePlayerSummaries() {
    withDatabase {
        val players = database.players.find().toList()

        players.forEachIndexed { index, player ->
            val duration = measureTimeMillis {
                recalculatePlayerSummary(player.uid)
            }

            logger.info("${index + 1} / ${players.size}: ${player.uid} took $duration ms")
        }
    }
}

