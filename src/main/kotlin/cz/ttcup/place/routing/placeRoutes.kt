package cz.ttcup.place.routing

import cz.ttcup.place.model.CreatePlaceByTournament
import cz.ttcup.place.model.GetPlacesRequest
import cz.ttcup.place.service.createPlaceFromTournament
import cz.ttcup.place.service.getPlaceRecommendations
import cz.ttcup.place.service.getPlaces
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.placeRoutes() {
    // getPlaces
    post {
        val request = call.receive<GetPlacesRequest>()

        getPlaces(request)
            .let { call.respond(it) }
    }
    // getPlaceRecommendations
    get("recommendations") {
        getPlaceRecommendations()
            .let { call.respond(it) }
    }
    // createPlaceByTournament
    post("by-tournament") {
        val request = call.receive<CreatePlaceByTournament>()

        createPlaceFromTournament(request)
            .let {
                call.respond(
                    it.insertedId?.asObjectId()?.value ?: throw IllegalStateException("Place not created")
                )
            }
    }
}

