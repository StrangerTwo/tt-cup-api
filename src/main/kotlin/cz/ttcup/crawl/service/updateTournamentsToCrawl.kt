package cz.ttcup.crawl.service

import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.tournament.service.findTournamentsIn
import cz.ttcup.util.toLocalDate
import cz.ttcup.util.toString
import org.jsoup.nodes.Document

suspend fun updateTournamentsToCrawl(doc: Document) {
    val table = doc.selectFirst("table.table tbody") ?: return;
    val playedTournaments = table.select("tr").map { row ->
        val onclick = row.attr("onclick")

        val (dateStr, hallNo) = "href='\\/[^\\/]+\\/[^\\/]+\\/(\\d{2}\\.\\d{2}\\.\\d{4})\\/(\\d+)\\/'".toRegex()
            .find(onclick)!!.destructured

        val date = dateStr.toLocalDate("dd.MM.yyyy")
        val place = "h:$hallNo"

        return@map Pair(date, place)
    }

    val existingTournaments = findTournamentsIn(playedTournaments)
    val crawlPages = playedTournaments
        .filterNot { (date, place) ->
            existingTournaments.any { t -> t.date == date && t.place == place }
        }
        .map { (date, place) ->
            CrawlPage("https://ttcup.com/schedule/${date.toString("dd.MM.yyyy")}/$place/")
        }

    saveCrawlPages(crawlPages)
}