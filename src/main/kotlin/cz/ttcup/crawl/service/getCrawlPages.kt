package cz.ttcup.crawl.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.model.GetPagesToCrawlRequest
import cz.ttcup.crawl.schema.crawlPages
import cz.ttcup.database.util.eq
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated
import kotlinx.coroutines.flow.toList

suspend fun getCrawlPages(request: GetPagesToCrawlRequest): PaginatedQueryResult<CrawlPage> {
    withDatabase {
        val playerCrawls = database.players
            .find(
                Player::recrawl eq true
            ).toList()
            .map { CrawlPage("https://ttcup.com/players/${it.uid}") }

        return database.crawlPages
            .queryPaginated(request.pageInfo).let {
                it.copy(
                    results = playerCrawls.plus(it.results),
                )
            }
    }
}