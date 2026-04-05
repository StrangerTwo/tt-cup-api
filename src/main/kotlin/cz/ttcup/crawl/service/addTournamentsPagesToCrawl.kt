package cz.ttcup.crawl.service

import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.player.model.Player
import org.jsoup.nodes.Document

suspend fun addTournamentsPagesToCrawl(doc: Document, player: Player) {
    val paginationList = doc.selectFirst(".pagination ul") ?: return;
    val maxPageItem = paginationList.select("li:not(.next)").last() ?: return;
    val maxPage = maxPageItem.text().trim().toInt();

    val pagesToCrawl = (2..maxPage).map {
        CrawlPage("https://ttcup.com/players/${player.uid}/$it/")
    }

    saveCrawlPages(pagesToCrawl)
}