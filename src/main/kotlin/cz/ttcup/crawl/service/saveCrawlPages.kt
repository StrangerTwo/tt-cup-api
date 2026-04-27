package cz.ttcup.crawl.service

import com.mongodb.client.model.UpdateOneModel
import com.mongodb.client.model.UpdateOptions
import cz.ttcup.context.service.withDatabase
import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.schema.crawlPages
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.logs.service.logger

suspend fun saveCrawlPages(crawlPages: List<CrawlPage>) {
    if (crawlPages.isEmpty()) return
    logger.info("Saving ${crawlPages.size} pages to crawl")
    withDatabase {
        database.crawlPages
            .bulkWrite(
                crawlPages
                    .map {
                        UpdateOneModel(
                            CrawlPage::url eq it.url,
                            CrawlPage::url set it.url,
                            UpdateOptions().upsert(true)
                        )
                    }
            )
    }
}