package cz.ttcup.place.service

import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.place.model.EditPlace
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.places

suspend fun editPlace(place: Place, request: EditPlace): UpdateResult {
    withDatabase {
        return database.places
            .updateOne(
                Place::uid eq place.uid,
                Updates.combine(
                    Place::location set request.location,
                )
            )
    }
}