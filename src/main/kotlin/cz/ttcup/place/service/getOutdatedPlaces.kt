package cz.ttcup.place.service

import com.mongodb.client.model.Filters
import cz.ttcup.database.util.lt
import cz.ttcup.database.util.ne
import cz.ttcup.place.model.OutdatedPlace
import cz.ttcup.place.model.Place
import cz.ttcup.util.toString
import java.time.LocalDate

suspend fun getOutdatedPlaces(): List<OutdatedPlace> {
    val places = getPlaces(
        Filters.and(
            Place::lastUpdate lt LocalDate.now().atStartOfDay(),
            Place::disabled ne true,
        )
    )

    return places.map {
        OutdatedPlace(
            it,
            "https://ttcup.com/schedule/${it.lastUpdate.plusDays(1).toString("dd.MM.yyyy")}/${it.uid}/"
        )
    }
}

