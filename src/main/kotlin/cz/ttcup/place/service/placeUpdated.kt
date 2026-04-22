package cz.ttcup.place.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.lt
import cz.ttcup.database.util.set
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.places
import cz.ttcup.tournament.model.Tournament

suspend fun placeUpdated(place: String, tournament: Tournament) {
    withDatabase {
        database.places
            .updateOne(
                Filters.and(
                    Place::uid eq place,
                    Place::lastUpdate lt tournament.date,
                ),
                Place::lastUpdate set tournament.date
            )
    }
}