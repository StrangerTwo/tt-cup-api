package cz.ttcup.place.service

import com.mongodb.client.model.Filters
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.lt
import cz.ttcup.place.model.OutdatedPlace
import cz.ttcup.place.model.Place
import cz.ttcup.util.toString
import java.time.LocalDate

suspend fun getOutdatedPlaces(): List<OutdatedPlace> {
    val places = getPlaces(
        Filters.and(
            Place::lastUpdate lt LocalDate.now().atStartOfDay(),
            Place::enabled eq true,
        )
    )

    return places
        .mapNotNull {
            OutdatedPlace(
                it,
                "https://ttcup.com/schedule/${
                    it.lastUpdate?.plusDays(1)?.toString("dd.MM.yyyy") ?: return@mapNotNull null
                }/${it.uid}/"
            )
        }
}

