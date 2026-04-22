package cz.ttcup.place.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.places
import java.time.LocalDate

suspend fun disablePlace(place: String, date: LocalDate) {
    withDatabase {
        database.places
            .updateOne(
                Filters.and(
                    Place::uid eq place,
                    Place::lastUpdate eq date.minusDays(1),
                ),
                Place::disabled set true
            )
    }
}