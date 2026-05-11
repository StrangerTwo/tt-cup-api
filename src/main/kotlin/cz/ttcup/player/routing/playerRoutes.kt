package cz.ttcup.player.routing

import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.service.saveCrawlPages
import cz.ttcup.player.context.player
import cz.ttcup.player.model.EditPlayer
import cz.ttcup.player.model.GetPlayerRivalsRequest
import cz.ttcup.player.model.GetPlayersRequest
import cz.ttcup.player.model.NewPlayer
import cz.ttcup.player.service.*
import cz.ttcup.summary.service.getSummary
import cz.ttcup.util.model.Status
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.playerRoutes() {
    // getPlayers
    post {
        val request = call.receive<GetPlayersRequest>()

        getPlayersSummaries(request)
            .let { call.respond(it) }
    }
    // newPlayer
    post("new") {
        val player = call.receive<NewPlayer>().let {
            it.copy(uid = it.uid.trim())
        }
        if (player.uid.isBlank() || player.uid.length < 5) throw IllegalArgumentException("Invalid uid")
        if (player.uid.contains(" ")) throw IllegalArgumentException("Nesmí obsahovat mezery")

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
            val playerUid = call.parameters["playerUid"] ?: throw IllegalArgumentException("playerUid can't be null")

            getPlayer(playerUid)
                ?.let { call.respond(it) }
                ?: call.respond(HttpStatusCode.NotFound)
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
        // getSummaryForPlayer
        get("summary") {
            val player = player()

            getSummary(player)
                ?.let { call.respond(it) }
                ?: call.respond(HttpStatusCode.NotFound)
        }
        route("rival") {
            // getPlayerRivals
            post {
                val player = player()
                val request = call.receive<GetPlayerRivalsRequest>()

                getPlayerRivals(player, request)
                    .let { call.respond(it) }
            }
            // getSummaryForRivalry
            get("{rivalUid}") {
                val playerUid =
                    call.parameters["playerUid"] ?: throw IllegalArgumentException("playerUid can't be null")
                val rivalUid = call.parameters["rivalUid"] ?: throw IllegalArgumentException("Rival uid not provided")

                getSummary(playerUid = playerUid, rivalUid = rivalUid)
                    ?.let { call.respond(it) }
                    ?: call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}

