package cz.ttcup.crawl.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.model.GetPagesToCrawlRequest
import cz.ttcup.crawl.schema.crawlPages
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.notRegex
import cz.ttcup.database.util.regex
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.model.plus
import cz.ttcup.util.service.queryPaginated

suspend fun getCrawlPages(request: GetPagesToCrawlRequest): PaginatedQueryResult<CrawlPage> {
    withDatabase {
        var result = database.players
            .queryPaginated(
                request.pageInfo,
                Filters.or(
                    Player::recrawl eq true,
                    Player::name eq null,
                )
            ).let {
                PaginatedQueryResult(
                    it.results.map { CrawlPage("https://ttcup.com/players/${it.uid}") },
                    it.pageInfo
                )
            }
        if (result.pageInfo.size >= request.pageInfo.size) return result;

        result += database.crawlPages
            .queryPaginated(
                request.pageInfo,
                CrawlPage::url regex "https://ttcup.com/schedule/.*".toRegex()
            )
        if (result.pageInfo.size >= request.pageInfo.size) return result;

        result += database.crawlPages
            .queryPaginated(
                request.pageInfo,
                CrawlPage::url notRegex "https://ttcup.com/schedule/.*".toRegex()
            )
        return result;
    }
}

