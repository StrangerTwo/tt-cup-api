package cz.ttcup.crawl.routing

import cz.ttcup.crawl.model.GetPagesToCrawlRequest
import cz.ttcup.crawl.service.getCrawlPage
import cz.ttcup.crawl.service.getCrawlPages
import cz.ttcup.util.model.PageInfoRequest
import cz.ttcup.util.model.Status
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.crawlRoutes() {
    // getPagesToCrawl
    get {
        val request = GetPagesToCrawlRequest(PageInfoRequest(0))
        getCrawlPages(request)
            .let { call.respond(it) }
    }
    // postPagesToCrawl
    post {
        val request = call.receive<GetPagesToCrawlRequest>()
        getCrawlPages(request)
            .let { call.respond(it) }
    }
    route("{url}") {
        // getIsCrawlPage
        get {
            val url = call.parameters["url"] ?: throw IllegalArgumentException("url can't be null")
            getCrawlPage(url).let { call.respond(Status(it != null)) }
        }
    }
}

