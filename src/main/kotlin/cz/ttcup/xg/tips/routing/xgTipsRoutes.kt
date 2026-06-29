package cz.ttcup.xg.tips.routing

import cz.ttcup.context.service.ctx
import cz.ttcup.util.model.Status
import cz.ttcup.xg.tips.model.UpdateFavouriteTip
import cz.ttcup.xg.tips.service.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.xgTipsRoutes() {
    // getTips
    get {
        getTips()
            .let { call.respond(it) }
    }
    // refreshTips
    post("refresh") {
        runTipScrapper()
        call.respond(Status(true))
    }
    // getFavouriteTips
    get("favourites") {
        getFavouriteTips(ctx.user?.uid ?: throw BadRequestException("Log in"))
            .let { call.respond(it) }
    }
    // favouriteTip
    patch("favourite/{tipId}") {
        val user = ctx.user ?: throw BadRequestException("Log in")
        val tipId = call.parameters["tipId"] ?: throw BadRequestException("Parameter tipId is required")
        val request = call.receive<UpdateFavouriteTip>()

        if (request.favourite) {
            setFavourite(user.uid, tipId, request)
        } else {
            removeFavourite(user.uid, tipId)
        }
        call.respond(Status(true))
    }
}

