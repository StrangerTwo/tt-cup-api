package cz.ttcup.openapi.routing

import cz.ttcup.openapi.service.getOpenApiYaml
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.openApiRouting() {
    // getOpenapi
    get("openapi.yaml") {
        call.response.header(HttpHeaders.ContentType, "text/yaml")
        call.respondText(getOpenApiYaml())
    }
}