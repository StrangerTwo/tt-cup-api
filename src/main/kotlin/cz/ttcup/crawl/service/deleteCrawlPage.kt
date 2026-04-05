package cz.ttcup.crawl.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.schema.crawlPages
import cz.ttcup.database.util.eq

suspend fun deleteCrawlPage(url: String) {
    withDatabase {
        database.crawlPages
            .deleteOne(CrawlPage::url eq url)
    }
}