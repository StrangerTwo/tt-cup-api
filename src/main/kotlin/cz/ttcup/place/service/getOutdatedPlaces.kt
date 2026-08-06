package cz.ttcup.place.service

import com.mongodb.client.model.Filters
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.lt
import cz.ttcup.place.model.OutdatedPlace
import cz.ttcup.place.model.Place
import cz.ttcup.tournament.service.getTournaments
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
        .flatMap {
            val days = it.lastUpdate?.toLocalDate()?.datesUntil(LocalDate.now().plusDays(1))?.toList()
                ?: return@flatMap listOf()
            val tournaments = getTournaments(it.uid, dateGte = it.lastUpdate.toLocalDate().plusDays(1))

            days
                .filter { day -> tournaments.none { t -> t.date == day } }
                .map { day ->
                    OutdatedPlace(
                        it,
                        "https://ttcup.com/schedule/${
                            day.toString("dd.MM.yyyy")
                        }/${it.uid}/"
                    )
                }
        }
}

