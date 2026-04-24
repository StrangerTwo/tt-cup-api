package cz.ttcup.player.context

import cz.ttcup.player.model.Player
import cz.ttcup.player.service.getPlayer
import io.ktor.server.plugins.*
import io.ktor.server.routing.*

suspend fun RoutingContext.player(): Player {
    val playerUid = call.parameters["playerUid"] ?: throw IllegalArgumentException("playerUid can't be null")

    return getPlayer(playerUid) ?: throw NotFoundException("Player not found")
}