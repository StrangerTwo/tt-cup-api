package cz.ttcup.system.module

import cz.ttcup.logs.service.logger
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<NotFoundException> { call, cause ->
            logger.error(cause.javaClass.name, cause);
            call.respond(HttpStatusCode.Companion.NotFound)
        }
        exception<NoSuchElementException> { call, cause ->
            logger.error(cause.javaClass.name, cause);
            call.respond(HttpStatusCode.Companion.NotFound)
        }
        exception<IllegalArgumentException> { call, cause ->
            logger.error(cause.javaClass.name, cause);
            call.respond(HttpStatusCode.Companion.BadRequest, cause.message ?: "")
        }
        exception<Throwable> { call, cause ->
//            this@configureStatusPages.sendDiscordMessage(
//                "Error on ${call.request.httpMethod.value} ${call.request.uri}:\n```\n${
//                    cause.stackTraceToString().substring(0, 800)
//                }\n```"
//            )
            logger.error("Internal Server Error", cause);
            call.respondText(text = "500: $cause", status = HttpStatusCode.Companion.InternalServerError)
        }
    }
}