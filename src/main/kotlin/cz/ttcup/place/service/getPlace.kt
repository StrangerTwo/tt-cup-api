package cz.ttcup.place.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.places
import kotlinx.coroutines.flow.firstOrNull

suspend fun getPlace(placeUid: String): Place? {
    withDatabase {
        return database.places
            .find(Place::uid eq placeUid)
            .firstOrNull()
    }
}