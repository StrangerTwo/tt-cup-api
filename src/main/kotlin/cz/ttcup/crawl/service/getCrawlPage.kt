package cz.ttcup.crawl.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.schema.crawlPages
import cz.ttcup.database.util.eq
import kotlinx.coroutines.flow.firstOrNull

suspend fun getCrawlPage(url: String): CrawlPage? {
    withDatabase {
        return database.crawlPages
            .find(CrawlPage::url eq url)
            .firstOrNull()
    }
}