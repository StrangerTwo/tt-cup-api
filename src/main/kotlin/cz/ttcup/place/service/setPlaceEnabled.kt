package cz.ttcup.place.service

import com.mongodb.client.result.UpdateResult
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.places

suspend fun setPlaceEnabled(place: Place, enabled: Boolean): UpdateResult {
    withDatabase {
        return database.places
            .updateOne(
                Place::uid eq place.uid,
                Place::enabled set enabled
            )
    }
}