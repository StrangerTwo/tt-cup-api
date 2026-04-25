package cz.ttcup.place.service

import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.places

suspend fun savePlace(place: Place) {
    withDatabase {
        database.places
            .updateOne(
                Place::uid eq place.uid,
                Updates.combine(
                    Place::name set place.name,
                    Place::startTime set place.startTime,
                ),
                UpdateOptions().upsert(true)
            )
    }
}