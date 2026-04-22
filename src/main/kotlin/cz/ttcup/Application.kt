package cz.ttcup

import cz.ttcup.admin.service.configureSecurity
import cz.ttcup.crawl.module.launchPlaceCrawler
import cz.ttcup.database.module.configureDatabases
import cz.ttcup.logs.module.configureLogging
import cz.ttcup.system.module.configureCORS
import cz.ttcup.system.module.configureRouting
import cz.ttcup.system.module.configureSerialization
import cz.ttcup.system.module.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    val port = System.getenv("PORT") ?: "8080"
    embeddedServer(Netty, port = port.toInt(), host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}


fun Application.module() {
    configureLogging()
    configureDatabases()

    configureCORS()
    configureSecurity()

    configureSerialization()
    configureStatusPages()
    configureRouting()

    launchPlaceCrawler()
}

