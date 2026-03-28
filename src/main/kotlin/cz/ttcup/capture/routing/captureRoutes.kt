package cz.ttcup.capture.routing

import cz.ttcup.capture.model.CaptureDto
import cz.ttcup.capture.service.capturePage
import cz.ttcup.logs.service.logger
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.captureRoutes() {
    // capturePage
    post() {
        val capture = call.receive<CaptureDto>()

        logger.info("Received html for url ${capture.url}: ${capture.html.length} chars")
        capturePage(capture);

        call.respond(HttpStatusCode.OK)
    }
}

