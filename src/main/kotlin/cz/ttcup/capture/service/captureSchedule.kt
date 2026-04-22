package cz.ttcup.capture.service

import cz.ttcup.capture.model.CaptureDto
import cz.ttcup.logs.service.logger
import cz.ttcup.place.service.disablePlace
import cz.ttcup.util.toLocalDate
import org.jsoup.Jsoup

suspend fun captureSchedule(capture: CaptureDto, restOfUrl: String) {
    val (dateStr, place) = "(\\d{2}\\.\\d{2}\\.\\d{4})/(\\w:\\d+)".toRegex().find(restOfUrl)?.destructured ?: return
    val date = dateStr.toLocalDate("dd.MM.yyyy")
    logger.info("Captured a schedule page ${capture.url}")
    val doc = Jsoup.parse(capture.html)
    val table = doc.selectFirst("table.table tbody")

    if (table != null) {
        captureTournament(table, date, place)
    } else if (capture.html.contains("404 PAGE NOT FOUND")) {
        disablePlace(place)
    }
}

