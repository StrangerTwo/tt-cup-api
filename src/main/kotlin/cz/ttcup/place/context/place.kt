package cz.ttcup.place.context

import cz.ttcup.place.model.Place
import cz.ttcup.place.service.getPlace
import io.ktor.server.plugins.*
import io.ktor.server.routing.*

suspend fun RoutingContext.place(): Place {
    val placeUid = call.parameters["placeUid"] ?: throw IllegalArgumentException("placeUid can't be null")

    return getPlace(placeUid) ?: throw NotFoundException()
}