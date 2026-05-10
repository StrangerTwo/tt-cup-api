package cz.ttcup.tournament.context

import cz.ttcup.tournament.model.TournamentFull
import cz.ttcup.tournament.service.getTournamentFull
import io.ktor.server.plugins.*
import io.ktor.server.routing.*

suspend fun RoutingContext.tournamentFull(
    players: Boolean = false
): TournamentFull {
    val tournamentId = call.parameters["tournamentId"] ?: throw IllegalArgumentException("tournamentId can't be null")

    return getTournamentFull(tournamentId, players = players) ?: throw NotFoundException()
}