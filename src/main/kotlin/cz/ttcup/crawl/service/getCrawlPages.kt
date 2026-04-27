package cz.ttcup.crawl.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.model.GetPagesToCrawlRequest
import cz.ttcup.crawl.schema.crawlPages
import cz.ttcup.database.util.notRegex
import cz.ttcup.database.util.regex
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.model.plus
import cz.ttcup.util.service.queryPaginated
import java.time.LocalDate
import kotlin.math.min

suspend fun getCrawlPages(request: GetPagesToCrawlRequest): PaginatedQueryResult<CrawlPage> {
    withDatabase {
        val newPlayerRegex = "https://ttcup.com/players/[^/]+/$".toRegex()
        val stisPlayerRegex = "https://stis.ping-pong.cz/hrac-.*".toRegex()
        val thisYearSchedulesRegex = "https://ttcup.com/schedule/.*${LocalDate.now().year}/.*".toRegex()
        val firstPagesOfPlayersRegex = "https://ttcup.com/players/.*/[1-6]/".toRegex()

        var result = database.crawlPages
            .queryPaginated(
                request.pageInfo,
                CrawlPage::url regex newPlayerRegex
            )
        result += database.crawlPages
            .queryPaginated(
                request.pageInfo,
                CrawlPage::url regex stisPlayerRegex
            )
        result += database.crawlPages
            .queryPaginated(
                request.pageInfo,
                CrawlPage::url regex thisYearSchedulesRegex
            )
        result += database.crawlPages
            .queryPaginated(
                request.pageInfo,
                CrawlPage::url regex firstPagesOfPlayersRegex
            )

        result += database.crawlPages
            .queryPaginated(
                request.pageInfo,
                Filters.and(
                    CrawlPage::url notRegex newPlayerRegex,
                    CrawlPage::url notRegex stisPlayerRegex,
                    CrawlPage::url notRegex thisYearSchedulesRegex,
                    CrawlPage::url notRegex firstPagesOfPlayersRegex,
                ),
                random = true
            )

        return result.copy(
            results = result.results.subList(0, request.pageInfo.size.coerceAtMost(result.results.size)),
            pageInfo = result.pageInfo.copy(
                size = min(request.pageInfo.size, result.results.size),
            )
        );
    }
}

