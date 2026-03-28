package cz.ttcup.admin.routing

import cz.ttcup.context.service.ctx
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes() {
    // getUser
    get {
        call.respond(ctx.user ?: throw IllegalStateException("User doesn't exists"))
    }
}