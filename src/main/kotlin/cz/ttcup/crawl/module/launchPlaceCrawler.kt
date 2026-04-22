package cz.ttcup.crawl.module

import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.service.saveCrawlPages
import cz.ttcup.logs.service.logger
import cz.ttcup.place.service.getOutdatedPlaces
import io.ktor.server.application.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.time.toKotlinDuration

fun Application.launchPlaceCrawler() {
    launch(newSingleThreadContext("Place Crawler")) {
        val durationUntilStart = Duration.between(
            LocalDateTime.now(),
            LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 5))
                .let { if (it.isBefore(LocalDateTime.now())) it.plusDays(1) else it }
        )

        logger.info("Place Crawler starting in: $durationUntilStart")
        delay(durationUntilStart.toKotlinDuration());

        saveCrawlPages(
            getOutdatedPlaces().map {
                CrawlPage(it.updateUrl)
            }
        )
    }.invokeOnCompletion { err ->
        if (err is CancellationException)
            return@invokeOnCompletion
        err?.let { logger.error("Place Crawler completed with error", it) }
        run {
            launchPlaceCrawler()
        }
    }
}