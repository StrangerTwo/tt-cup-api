package cz.ttcup.place.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.places

suspend fun disablePlace(place: String) {
    withDatabase {
        database.places
            .updateOne(
                Place::uid eq place,
                Place::disabled set true
            )
    }
}