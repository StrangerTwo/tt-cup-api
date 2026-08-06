package cz.ttcup

import cz.ttcup.admin.service.configureSecurity
import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.module.launchPageCrawlers
import cz.ttcup.crawl.service.saveCrawlPages
import cz.ttcup.database.module.configureDatabaseIndexes
import cz.ttcup.database.module.configureDatabases
import cz.ttcup.logs.module.configureLogging
import cz.ttcup.place.service.getOutdatedPlaces
import cz.ttcup.system.module.configureCORS
import cz.ttcup.system.module.configureRouting
import cz.ttcup.system.module.configureSerialization
import cz.ttcup.system.module.configureStatusPages
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking

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

    configureDatabaseIndexes()

    runBlocking {
        saveCrawlPages(
            getOutdatedPlaces().map {
                CrawlPage(it.updateUrl)
            }
        )
    }

    launchPageCrawlers()
}

