package cz.ttcup.capture.service

import cz.ttcup.capture.model.CaptureDto
import cz.ttcup.logs.service.logger
import cz.ttcup.player.service.getPlayer

suspend fun capturePlayer(capture: CaptureDto) {
    val restOfUrl = capture.url.substring("https://ttcup.com/players/".length + 1)
    val uid = restOfUrl.substring(0, restOfUrl.indexOf("/"))
    val player = getPlayer(uid) ?: return

    logger.info("Captured player $uid page ${capture.url}")
}