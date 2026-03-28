package cz.ttcup.system.module

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCORS() {
    install(CORS) {
        allowMethod(HttpMethod.Companion.Get)
        allowMethod(HttpMethod.Companion.Post)
        allowMethod(HttpMethod.Companion.Options)
        allowMethod(HttpMethod.Companion.Put)
        allowMethod(HttpMethod.Companion.Delete)
        allowMethod(HttpMethod.Companion.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)

        // DEV
        allowHost("localhost:5463")

        // PROD
        allowHost("ttcup.dev.mhdonline.cz", listOf("https"))
    }
}