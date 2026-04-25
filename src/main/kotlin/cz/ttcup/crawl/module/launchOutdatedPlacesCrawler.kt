package cz.ttcup.crawl.module

import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.service.saveCrawlPages
import cz.ttcup.place.service.getOutdatedPlaces
import cz.ttcup.util.module.launchRecursive
import io.ktor.server.application.*
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun Application.launchOutdatedPlacesCrawler() {
    launchRecursive(
        "Outdated Places Crawler",
        delayFn = {
            Duration.between(
                LocalDateTime.now(),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 5))
                    .let { if (it.isBefore(LocalDateTime.now())) it.plusDays(1) else it }
            )
        }
    ) {
        saveCrawlPages(
            getOutdatedPlaces().map {
                CrawlPage(it.updateUrl)
            }
        )
    }
}