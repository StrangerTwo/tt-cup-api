package cz.ttcup.place.service

import cz.ttcup.database.util.lt
import cz.ttcup.place.model.OutdatedPlace
import cz.ttcup.place.model.Place
import cz.ttcup.util.toString
import java.time.LocalDate

suspend fun getOutdatedPlaces(): List<OutdatedPlace> {
    val places = getPlaces(
        Place::lastUpdate lt LocalDate.now().atStartOfDay()
    )

    return places.map {
        OutdatedPlace(
            it,
            "https://ttcup.com/schedule/${it.lastUpdate.plusDays(1).toString("dd.MM.yyyy")}/${it.uid}/"
        )
    }
}

