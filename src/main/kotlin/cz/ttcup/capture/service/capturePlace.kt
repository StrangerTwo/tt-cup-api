package cz.ttcup.capture.service

import cz.ttcup.place.model.Place
import cz.ttcup.place.service.savePlace
import cz.ttcup.util.toLocalTime
import org.jsoup.nodes.Element

suspend fun capturePlace(it: Element) {
    val href = it.attr("href") ?: return
    val (dateStr, place) = "/schedule/(\\d{2}\\.\\d{2}\\.\\d{4})/(\\w:\\d+)".toRegex().find(href)?.destructured
        ?: return
    val name = it.selectFirst("h5")?.text()?.trim() ?: return
    val startTimeStr = it.selectFirst(".flag-data div")?.text()?.trim() ?: return
    val startTime = startTimeStr.toLocalTime("HH:mm")

    savePlace(Place(place, name, startTime))
}