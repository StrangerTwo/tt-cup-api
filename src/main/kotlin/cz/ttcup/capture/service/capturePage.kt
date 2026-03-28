package cz.ttcup.capture.service

import cz.ttcup.capture.model.CaptureDto

suspend fun capturePage(capture: CaptureDto) {
    when {
        capture.url.startsWith("https://ttcup.com/players/") -> {
            capturePlayer(capture)
        }
    }
}