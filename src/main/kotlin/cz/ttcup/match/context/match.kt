package cz.ttcup.match.context

import cz.ttcup.match.model.Match
import cz.ttcup.match.service.getMatch
import io.ktor.server.plugins.*
import io.ktor.server.routing.*

suspend fun RoutingContext.match(): Match {
    val matchId = call.parameters["matchId"] ?: throw IllegalArgumentException("matchId can't be null")

    return getMatch(matchId) ?: throw NotFoundException()
}