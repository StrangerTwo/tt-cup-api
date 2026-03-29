package cz.ttcup.capture.service

import cz.ttcup.capture.model.CaptureDto
import cz.ttcup.logs.service.logger
import cz.ttcup.player.service.getPlayer
import cz.ttcup.player.service.updatePlayer
import org.jsoup.Jsoup

suspend fun capturePlayer(capture: CaptureDto, restOfUrl: String) {
    val uid = restOfUrl.substring(0, restOfUrl.indexOf("/"))
    val player = getPlayer(uid) ?: return

    logger.info("Captured player $uid page ${capture.url}")

    val doc = Jsoup.parse(capture.html)
    doc.selectFirst("h1.h2")?.let { player.name = it.text().trim() }
    doc.selectFirst(".single-players-info--top--content .red")?.let { player.elo = it.text().trim().toInt() }

    updatePlayer(player);
}
