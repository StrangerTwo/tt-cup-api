package cz.ttcup.crawl.model

import kotlinx.serialization.Serializable

@Serializable
data class CrawlPage(
    val url: String,
)