package cz.ttcup.crawl.module

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.service.saveCrawlPages
import cz.ttcup.database.util.*
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.PLACES
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.schema.tournaments
import cz.ttcup.util.module.launchRecursive
import cz.ttcup.util.toString
import io.ktor.server.application.*
import kotlinx.coroutines.flow.toList
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun Application.launchOngoingTournamentsCrawler() {
    launchRecursive(
        "Ongoing Tournaments Crawler",
        delayFn = {
            Duration.between(
                LocalDateTime.now(),
                LocalDateTime.now()
                    .let {
                        val minute = when {
                            it.minute < 5 -> 5
                            it.minute < 35 -> 35
                            else -> 5
                        }
                        it.withMinute(minute)
                    }
                    .let {
                        if (it.isBefore(LocalDateTime.now())) it.plusHours(1) else it
                    }
            )
        }
    ) {
        ongoingTournamentsCrawler()
    }
}

suspend fun ongoingTournamentsCrawler() {
    val tournaments = withDatabase {
        database.tournaments
            .aggregate<Tournament>(
                listOf(
                    Aggregates.match(
                        Filters.and(
                            Tournament::done ne true,
                            Tournament::date eq LocalDate.now()
                        )
                    ),
                    *lookupFirst(
                        PLACES,
                        Tournament::place.fieldName,
                        Place::uid.fieldName,
                        "fullPlace",
                    ),
                    Aggregates.match(
                        Filters.and(
                            "fullPlace.${Place::startTime.fieldName}" lt LocalTime.now().minusMinutes(5),
                        )
                    ),
                )
            )
            .toList()
    }

    saveCrawlPages(
        tournaments.map {
            CrawlPage("https://ttcup.com/schedule/${it.date.toString("dd.MM.yyyy")}/${it.place}/")
        }
    )
}


