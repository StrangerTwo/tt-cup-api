package cz.ttcup.crawl.model

import cz.ttcup.util.model.PageInfoRequest
import kotlinx.serialization.Serializable

@Serializable
data class GetPagesToCrawlRequest(
    val pageInfo: PageInfoRequest
)