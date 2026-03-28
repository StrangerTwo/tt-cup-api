package cz.ttcup.system.module

import cz.ttcup.admin.routing.userRoutes
import cz.ttcup.capture.routes.captureRoutes
import cz.ttcup.context.service.interceptContext
import cz.ttcup.openapi.routing.openApiRouting
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authenticate(
            optional = true
        ) {
            interceptContext()
            install(defaultSerializationPlugin)
            // getHello
            get("/") {
                val target = System.getenv("TARGET") ?: "World"
                call.respondText("Hello, $target !", ContentType.Text.Html)
            }
            route("user") {
                userRoutes()
            }
            route("capture") {
                captureRoutes()
            }
            openApiRouting()
            get("{...}") {
                call.respondText("Hello from ${call.request.uri}", ContentType.Text.Html)
            }
        }
    }
}

