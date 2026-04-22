package cz.ttcup.capture.service

import cz.ttcup.capture.model.CaptureDto
import cz.ttcup.crawl.service.addTournamentsPagesToCrawl
import cz.ttcup.crawl.service.updateTournamentsToCrawl
import cz.ttcup.logs.service.logger
import cz.ttcup.player.service.getPlayer
import cz.ttcup.player.service.updatePlayer
import org.jsoup.Jsoup

suspend fun capturePlayer(capture: CaptureDto, restOfUrl: String) {
    val uid = restOfUrl.substring(0, restOfUrl.indexOf("/"))
    val player = getPlayer(uid) ?: return
    val recrawlPages = player.name == null || player.recrawl == true;

    logger.info("Captured player $uid page ${capture.url}, recrawl: $recrawlPages")

    val doc = Jsoup.parse(capture.html)
    val name = doc.selectFirst("h1.h2")?.text()?.trim()
    val ranking = doc.selectFirst(".single-players-info--top--content .red")?.text()?.trim()?.toInt()

    if (name == null && ranking == null) return;
    updatePlayer(
        uid,
        name = name,
        ranking = ranking,
    );
    if (recrawlPages) {
        addTournamentsPagesToCrawl(doc, player)
    }

    updateTournamentsToCrawl(doc)
}