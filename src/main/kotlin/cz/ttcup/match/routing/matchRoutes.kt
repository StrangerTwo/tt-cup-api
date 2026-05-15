package cz.ttcup.match.routing

import cz.ttcup.match.model.GetMatchesRequest
import cz.ttcup.match.model.GetTopMatchesRequest
import cz.ttcup.match.model.SetTopMatchConfigRequest
import cz.ttcup.match.service.getMatchFilters
import cz.ttcup.match.service.getMatches
import cz.ttcup.match.service.getTopMatches
import cz.ttcup.match.service.setMatchFilters
import cz.ttcup.util.model.Status
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.matchRoutes() {
    // getMatches
    post {
        val request = call.receive<GetMatchesRequest>()
        getMatches(request)
            .let { call.respond(it) }
    }
    route("top") {
        // getTopMatches
        post {
            val request = call.receive<GetTopMatchesRequest>()

            getTopMatches(request)
                .let { call.respond(it) }
        }
        route("config") {
            // getMatchFilters
            get {
                getMatchFilters()
                    .let { call.respond(it) }
            }
            // setMatchFilters
            put {
                val config = call.receive<SetTopMatchConfigRequest>()

                setMatchFilters(config)
                    .let { call.respond(Status(true)) }
            }
        }
    }
}

