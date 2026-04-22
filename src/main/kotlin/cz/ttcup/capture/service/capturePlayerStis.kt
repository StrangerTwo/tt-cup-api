package cz.ttcup.capture.service

import cz.ttcup.capture.model.CaptureDto
import cz.ttcup.logs.service.logger
import cz.ttcup.player.service.getPlayer
import cz.ttcup.player.service.updatePlayer
import org.jsoup.Jsoup

suspend fun capturePlayerStis(capture: CaptureDto, restOfUrl: String) {
    val stisId = restOfUrl.takeWhile { it != '/' }
    val player = getPlayer(stisId = stisId) ?: return

    logger.info("Captured player ${player.uid} stis page ${capture.url}")

    val doc = Jsoup.parse(capture.html)
    val strText = doc.selectFirst(".elostat_detail h4")?.text() ?: return
    val elo = "STR: (\\d+)".toRegex().find(strText)?.destructured?.component1()?.toIntOrNull() ?: return

    updatePlayer(
        player.uid,
        elo = elo,
    )
}