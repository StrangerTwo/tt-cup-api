package cz.ttcup.util.service

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Facet
import com.mongodb.client.model.Projections
import com.mongodb.client.model.Sorts
import com.mongodb.kotlin.client.coroutine.MongoCollection
import cz.ttcup.util.model.PageInfo
import cz.ttcup.util.model.PageInfoRequest
import cz.ttcup.util.model.PaginatedQueryResult
import kotlinx.coroutines.flow.first
import org.bson.BsonDocument
import org.bson.BsonDocumentReader
import org.bson.codecs.DecoderContext
import org.bson.conversions.Bson

suspend inline fun <reified T : Any> MongoCollection<T>.queryPaginated(
    pageInfo: PageInfoRequest,
    filter: Bson? = null,
    preFilterAggregate: List<Bson> = emptyList(),
    postSortAggregate: List<Bson> = emptyList(),
    random: Boolean = false,
    logDebug: Boolean = false,
): PaginatedQueryResult<T> {
    val result = this
        .aggregate<IntPaginatedQueryResult>(
            listOfNotNull(
                *preFilterAggregate.toTypedArray(),
                filter?.let { Aggregates.match(it) },
                pageInfo.sort?.let {
                    Aggregates.sort(
                        Sorts.orderBy(
                            pageInfo.sort.map {
                                it.split(":").let { (key, direction) ->
                                    if (direction == "asc") Sorts.ascending(key) else Sorts.descending(key)
                                }
                            }
                        )
                    )
                },
                *postSortAggregate.toTypedArray(),
                Aggregates.facet(
                    Facet(
                        "results",
                        if (random) listOf(
                            Aggregates.sample(pageInfo.size)
                        ) else listOf(
                            Aggregates.skip(pageInfo.page * pageInfo.size),
                            Aggregates.limit(pageInfo.size)
                        )
                    ),
                    Facet(
                        "totalElements",
                        Aggregates.count()
                    )
                ),
                Aggregates.project(
                    Projections.fields(
                        Projections.include("results"),
                        Projections.computed(
                            "totalElements",
                            Projections.computed("\$first", "\$totalElements.count")
                        )
                    )
                )
            ).let {
                if (logDebug) println(it.joinToString(",\n") { it.toBsonDocument().toJson() })
                it
            },
        ).first()

    val codec = codecRegistry.get(T::class.java)
    return PaginatedQueryResult(
        result.results
            .map { codec.decode(BsonDocumentReader(it), DecoderContext.builder().build()) },
        PageInfo(pageInfo, result)
    )
}

data class IntPaginatedQueryResult(
    val results: List<BsonDocument>,
    val totalElements: Long? = null,
)