package cz.ttcup.crawl.schema

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import cz.ttcup.crawl.model.CrawlPage

const val CRAWL_PAGES = "crawl-pages"
val MongoDatabase.crawlPages
    get() = this.getCollection<CrawlPage>(CRAWL_PAGES)