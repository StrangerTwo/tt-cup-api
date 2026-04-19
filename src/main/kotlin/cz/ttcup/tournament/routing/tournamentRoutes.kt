package cz.ttcup.tournament.routing

import cz.ttcup.tournament.context.tournament
import cz.ttcup.tournament.context.tournamentFull
import cz.ttcup.tournament.model.GetTournamentsRequest
import cz.ttcup.tournament.service.getTournaments
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.tournamentRoutes() {
    // getTournaments
    post {
        val request = call.receive<GetTournamentsRequest>()

        getTournaments(request)
            .let { call.respond(it) }
    }
    route("{tournamentId}") {
        // getTournament
        get {
            tournament().let { call.respond(it) }
        }
        // getTournamentFull
        get("full") {
            tournamentFull().let { call.respond(it) }
        }
    }
}

