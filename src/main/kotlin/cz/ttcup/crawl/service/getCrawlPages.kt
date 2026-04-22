package cz.ttcup.crawl.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.model.GetPagesToCrawlRequest
import cz.ttcup.crawl.schema.crawlPages
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.notRegex
import cz.ttcup.database.util.regex
import cz.ttcup.place.service.getOutdatedPlaces
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.model.plus
import cz.ttcup.util.service.queryPaginated
import java.time.LocalDate
import kotlin.math.min

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

        result += PaginatedQueryResult(
            getOutdatedPlaces().map {
                CrawlPage(it.updateUrl)
            }
        )

        val thisYearSchedulesRegex = "https://ttcup.com/schedule/.*${LocalDate.now().year}/.*".toRegex()
        val first10PagesOfPlayersRegex = "https://ttcup.com/players/.*/[1-6]/".toRegex()
        result += database.crawlPages
            .queryPaginated(
                request.pageInfo,
                CrawlPage::url regex thisYearSchedulesRegex
            )
        result += database.crawlPages
            .queryPaginated(
                request.pageInfo,
                CrawlPage::url regex first10PagesOfPlayersRegex
            )

        result += database.crawlPages
            .queryPaginated(
                request.pageInfo,
                Filters.and(
                    CrawlPage::url notRegex thisYearSchedulesRegex,
                    CrawlPage::url notRegex first10PagesOfPlayersRegex,
                ),
                random = true
            )

        return result.copy(
            results = result.results.subList(0, request.pageInfo.size),
            pageInfo = result.pageInfo.copy(
                size = min(request.pageInfo.size, result.results.size),
            )
        );
    }
}

