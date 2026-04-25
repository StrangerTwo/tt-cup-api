package cz.ttcup.crawl.module

import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.service.saveCrawlPages
import cz.ttcup.tournament.service.getTournaments
import cz.ttcup.util.module.launchRecursive
import cz.ttcup.util.toString
import io.ktor.server.application.*
import java.time.Duration
import java.time.LocalDateTime

fun Application.launchUndoneTournamentsCrawler() {
    launchRecursive(
        "Undone Tournaments Crawler",
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
        saveCrawlPages(
            getTournaments(done = false).map {
                CrawlPage("https://ttcup.com/schedule/${it.date.toString("dd.MM.yyyy")}/${it.place}/")
            }
        )
    }
}


