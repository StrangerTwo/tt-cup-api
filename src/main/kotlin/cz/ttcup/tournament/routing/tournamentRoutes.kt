package cz.ttcup.tournament.routing

import cz.ttcup.tournament.context.tournamentFull
import cz.ttcup.tournament.model.GetTournamentsRequest
import cz.ttcup.tournament.model.GetTournamentsTodayRequest
import cz.ttcup.tournament.service.getTournamentFull
import cz.ttcup.tournament.service.getTournaments
import cz.ttcup.tournament.service.getTournamentsToday
import cz.ttcup.util.toLocalDate
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDate

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
            val players = call.queryParameters["players"]?.toBoolean() ?: false
            tournamentFull(players).let { call.respond(it) }
        }
    }
    // getQueryTournament
    get {
        val place = call.queryParameters["place"] ?: throw IllegalArgumentException("place can't be null")
        val date = call.queryParameters["date"]?.toLocalDate("yyyy-MM-dd") ?: LocalDate.now()
        val players = call.queryParameters["players"]?.toBoolean() ?: false

        getTournamentFull(place = place, date = date, players = players)
            ?.let { call.respond(it) }
            ?: call.respond(HttpStatusCode.NotFound)
    }
    // getTournamentsToday
    post("today") {
        val request = call.receive<GetTournamentsTodayRequest>()

        getTournamentsToday(request)
            .let { call.respond(it) }
    }
}

