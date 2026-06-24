package cz.ttcup.capture.service

import cz.ttcup.behavior.service.recalculatePlayerBehavior
import cz.ttcup.capture.model.CaptureDto
import cz.ttcup.logs.service.logger
import cz.ttcup.place.service.placeUpdated
import cz.ttcup.summary.service.recalculatePlayerSummary
import cz.ttcup.util.toLocalDate
import org.jsoup.Jsoup
import java.time.LocalTime

suspend fun captureSchedule(capture: CaptureDto, restOfUrl: String) {
    val (dateStr, place) = "(\\d{2}\\.\\d{2}\\.\\d{4})/(\\w:\\d+)".toRegex().find(restOfUrl)?.destructured ?: return
    val date = dateStr.toLocalDate("dd.MM.yyyy")
    logger.info("Captured a schedule page ${capture.url}")
    val doc = Jsoup.parse(capture.html)

    doc.select(".tournaments_on--single").forEach {
        capturePlace(it)
    }
    doc.selectFirst("table.table tbody")?.let {
        captureTournament(it, date, place)?.let { (tournament, players) ->
            placeUpdated(place, tournament.date.atTime(LocalTime.now()))

            players.forEach { player ->
                recalculatePlayerSummary(player.uid)
            }

            if (tournament.done == true) {
                players.forEach { player ->
                    recalculatePlayerBehavior(player.uid)
                }
            }
        }
    }
}

