package cz.ttcup.player.routing

import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.service.saveCrawlPages
import cz.ttcup.player.context.player
import cz.ttcup.player.model.GetPlayersRequest
import cz.ttcup.player.model.NewPlayer
import cz.ttcup.player.service.*
import cz.ttcup.util.model.Status
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
            .let { call.respond(Status(it.insertedId != null)) }
        saveCrawlPages(listOf(CrawlPage("https://ttcup.com/players/${player.uid}/")))
    }
    route("{playerUid}") {
        // getPlayer
        get {
            player().let { call.respond(it) }
        }
        // setPlayerFavourite
        patch("favourite") {
            val request = call.receive<Status>()
            val player = player()

            setPlayerFavourite(player, request.status)
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
