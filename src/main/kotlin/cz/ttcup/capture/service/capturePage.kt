package cz.ttcup.capture.service

import cz.ttcup.capture.model.CaptureDto
import cz.ttcup.crawl.service.deleteCrawlPage

suspend fun capturePage(capture: CaptureDto) {
    when {
        capture.url.startsWith("https://ttcup.com/players/") -> {
            capturePlayer(capture, capture.url.substring("https://ttcup.com/players/".length))
        }

        capture.url.startsWith("https://ttcup.com/schedule/") -> {
            captureSchedule(capture, capture.url.substring("https://ttcup.com/players/".length))
        }
    }
    deleteCrawlPage(capture.url)
}
