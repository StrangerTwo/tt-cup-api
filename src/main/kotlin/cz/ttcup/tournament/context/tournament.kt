package cz.ttcup.tournament.context

import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.service.getTournament
import io.ktor.server.plugins.*
import io.ktor.server.routing.*

suspend fun RoutingContext.tournament(): Tournament {
    val tournamentId = call.parameters["tournamentId"] ?: throw IllegalArgumentException("tournamentId can't be null")

    return getTournament(tournamentId) ?: throw NotFoundException()
}