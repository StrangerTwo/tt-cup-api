package cz.ttcup.player.routing

import cz.ttcup.player.model.NewPlayer
import cz.ttcup.player.service.getPlayers
import cz.ttcup.player.service.saveNewPlayer
import cz.ttcup.util.model.Status
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.playerRoutes() {
    // getPlayers
    get() {
        getPlayers()
            .let { call.respond(it) }
    }
    // newPlayer
    post("new") {
        val player = call.receive<NewPlayer>()

        saveNewPlayer(player)
            .let { call.respond(Status(it.insertedId != null)) }
    }
}

