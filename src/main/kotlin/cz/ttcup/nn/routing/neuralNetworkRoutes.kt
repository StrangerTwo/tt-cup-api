package cz.ttcup.nn.routing

import cz.ttcup.nn.service.getNeuralNetworkMatchData
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.neuralNetworkRoutes() {
    // getNeuralNetworkMatchData
    get {
        getNeuralNetworkMatchData()
            .let { call.respond(it) }
    }
}
