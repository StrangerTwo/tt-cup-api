package cz.ttcup.capture.service

import cz.ttcup.capture.model.CaptureDto
import cz.ttcup.logs.service.logger
import cz.ttcup.util.toLocalDate
import org.jsoup.Jsoup

suspend fun captureSchedule(capture: CaptureDto, restOfUrl: String) {
    val (dateStr, place) = "(\\d{2}\\.\\d{2}\\.\\d{4})/(\\w:\\d+)".toRegex().find(restOfUrl)?.destructured ?: return
    val date = dateStr.toLocalDate("dd.MM.yyyy")
    logger.info("Captured a schedule page ${capture.url}")
    val doc = Jsoup.parse(capture.html)

    doc.select(".tournaments_on--single").forEach {
        capturePlace(it)
    }
    doc.selectFirst("table.table tbody")?.let {
        captureTournament(it, date, place)
    }
}

