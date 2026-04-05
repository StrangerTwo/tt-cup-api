package cz.ttcup.crawl.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.model.GetPagesToCrawlRequest
import cz.ttcup.crawl.schema.crawlPages
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated

suspend fun getCrawlPages(request: GetPagesToCrawlRequest): PaginatedQueryResult<CrawlPage> {
    withDatabase {
        return database.crawlPages
            .queryPaginated(request.pageInfo)
    }
}