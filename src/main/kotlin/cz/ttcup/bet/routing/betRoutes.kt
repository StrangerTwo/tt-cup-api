package cz.ttcup.bet.routing

import cz.ttcup.bet.model.GetBetsRequest
import cz.ttcup.bet.model.NewBet
import cz.ttcup.bet.service.getBet
import cz.ttcup.bet.service.getBets
import cz.ttcup.bet.service.getTournamentBets
import cz.ttcup.bet.service.setBet
import cz.ttcup.match.context.match
import cz.ttcup.place.service.getPlace
import cz.ttcup.tournament.context.tournament
import cz.ttcup.tournament.service.getTournament
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.betRoutes() {
    // getBets
    post {
        val request = call.receive<GetBetsRequest>()

        getBets(request)
            .let { call.respond(it) }
    }
    route("{matchId}") {
        // getBet
        get {
            val match = match()
            getBet(matchId = match.objectId)
                ?.let { call.respond(it) }
                ?: call.respond(HttpStatusCode.NotFound)
        }
        // setBet
        patch {
            val match = match()
            val tournament = getTournament(match.tournamentId) ?: throw IllegalArgumentException("Tournament not found")
            val place = getPlace(tournament.place) ?: throw IllegalArgumentException("Place not found")

            val request = call.receive<NewBet>()

            setBet(match, tournament, place, request)
                .let { call.respond(it) }
        }
    }
    route("{tournamentId}") {
        // getTournamentBets
        get {
            val tournament = tournament()

            getTournamentBets(tournament)
                .let { call.respond(it) }
        }
    }

}

