package cz.ttcup.place.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.exists
import cz.ttcup.database.util.lt
import cz.ttcup.database.util.set
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.places
import java.time.LocalDateTime

suspend fun placeUpdated(place: String, date: LocalDateTime) {
    withDatabase {
        database.places
            .updateOne(
                Filters.and(
                    Place::uid eq place,
                    Filters.or(
                        Place::lastUpdate lt date,
                        Place::lastUpdate.exists(false),
                    )
                ),
                Place::lastUpdate set date
            )
    }
}