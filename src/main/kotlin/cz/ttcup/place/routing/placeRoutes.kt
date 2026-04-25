package cz.ttcup.place.routing

import cz.ttcup.place.context.place
import cz.ttcup.place.model.EditPlace
import cz.ttcup.place.model.GetPlacesRequest
import cz.ttcup.place.service.editPlace
import cz.ttcup.place.service.getOutdatedPlaces
import cz.ttcup.place.service.getPlaces
import cz.ttcup.place.service.setPlaceEnabled
import cz.ttcup.util.model.Status
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
        // getPlace
        get {
            try {
                place().let { call.respond(it) }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // setPlaceEnabled
        patch("enabled") {
            val request = call.receive<Status>()
            val place = place()

            setPlaceEnabled(place, request.status)
                .let { call.respond(Status(it.modifiedCount == 1L)) }
        }
        // editPlace
        patch {
            val place = place()
            val request = call.receive<EditPlace>()

            editPlace(place, request)
                .let { call.respond(Status(it.modifiedCount == 1L)) }
        }
    }
    // getOutdatedPlaces
    get("outdated") {
        getOutdatedPlaces()
            .let { call.respond(it) }
    }
}

