package cz.ttcup.player.routing

import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.service.saveCrawlPages
import cz.ttcup.player.context.player
import cz.ttcup.player.model.EditPlayer
import cz.ttcup.player.model.GetPlayersRequest
import cz.ttcup.player.model.NewPlayer
import cz.ttcup.player.service.*
import cz.ttcup.util.model.Status
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.playerRoutes() {
    // getPlayers
    post {
        val request = call.receive<GetPlayersRequest>()

        getPlayers(request)
            .let { call.respond(it) }
    }
    // newPlayer
    post("new") {
        val player = call.receive<NewPlayer>().let {
            it.copy(uid = it.uid.trim())
        }
        if (player.uid.isBlank() || player.uid.length < 5) throw IllegalArgumentException("Invalid uid")

        saveNewPlayer(player)
            .let {
                call.respond(
                    it.insertedId?.asObjectId()?.value ?: throw IllegalStateException("Player not created")
                )
            }
        saveCrawlPages(listOf(CrawlPage("https://ttcup.com/players/${player.uid}/")))
    }
    route("{playerUid}") {
        // getPlayer
        get {
            try {
                player().let { call.respond(it) }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // setPlayerFavourite
        patch("favourite") {
            val request = call.receive<Status>()
            val player = player()

            setPlayerFavourite(player, request.status)
                .let { call.respond(Status(it.modifiedCount == 1L)) }
        }
        // editPlayer
        patch {
            val player = player()
            val request = call.receive<EditPlayer>()

            editPlayer(player, request)
                .let { call.respond(Status(it.modifiedCount == 1L)) }
        }
        // getPlayerMatchesSummary
        get("summary") {
            val player = player()

            getPlayerMatchesSummary(player)
                .let { call.respond(it) }
        }
        // getPlayerRivals
        get("rivals") {
            val player = player()

            getPlayerRivals(player)
                .let { call.respond(it) }
        }
    }
}

