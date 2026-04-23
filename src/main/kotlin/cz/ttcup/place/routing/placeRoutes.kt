package cz.ttcup.place.routing

import cz.ttcup.place.context.place
import cz.ttcup.place.model.CreatePlaceByTournament
import cz.ttcup.place.model.GetPlaceRecommendationsRequest
import cz.ttcup.place.model.GetPlacesRequest
import cz.ttcup.place.service.createPlaceFromTournament
import cz.ttcup.place.service.getOutdatedPlaces
import cz.ttcup.place.service.getPlaceRecommendations
import cz.ttcup.place.service.getPlaces
import io.ktor.http.*
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
    route("{placeUid}") {
        get {
            try {
                place().let { call.respond(it) }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
    // getPlaceRecommendations
    post("recommendations") {
        val request = call.receive<GetPlaceRecommendationsRequest>()

        getPlaceRecommendations(request)
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
    // getOutdatedPlaces
    get("outdated") {
        getOutdatedPlaces()
            .let { call.respond(it) }
    }
}

