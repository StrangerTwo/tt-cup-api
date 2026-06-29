package cz.ttcup.xg.tips.service

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.BsonField
import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.logs.service.logger
import cz.ttcup.system.module.ktorClient
import cz.ttcup.xg.tips.model.FavouriteTip
import cz.ttcup.xg.tips.model.RemovedTipIds
import cz.ttcup.xg.tips.model.XgForecast
import cz.ttcup.xg.tips.model.XgTip
import cz.ttcup.xg.tips.schema.XG_TIPS
import cz.ttcup.xg.tips.schema.favouriteTips
import cz.ttcup.xg.tips.schema.xgTips
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.toList
import org.bson.BsonDocument
import org.bson.BsonString

suspend fun runTipScrapper() {
    val lock = acquireTipScrapperLock()
    try {
        doRunTipScrapper()
    } finally {
        releaseTipScrapperLock(lock)
    }
}


private suspend fun doRunTipScrapper() {
    val res = ktorClient.get {
        url("https://api.xgscore.io/forecasts/coming/public?period=week")
        contentType(ContentType.Application.Json)
    }

    val forecasts = res.body<List<XgForecast>>()

    withDatabase {
        database.xgTips.deleteMany(Filters.empty())
        val tips = forecasts.map { XgTip(it) }
        logger.info("Saving ${tips.size} new tips")
        database.xgTips.insertMany(
            tips
        )

        val tipIds = database.favouriteTips
            .aggregate<RemovedTipIds>(
                listOf(
                    Aggregates.lookup(
                        XG_TIPS,
                        FavouriteTip::tipId.name,
                        XgTip::id.name,
                        "joined"
                    ),
                    Aggregates.match(
                        Filters.exists("joined.0", false)
                    ),
                    Aggregates.group(
                        null,
                        BsonField(
                            "tipIds",
                            BsonDocument(
                                "\$push", BsonString("\$tipId")
                            )
                        )
                    )
                )
            )
            .toList()
            .flatMap { it.tipIds }

        database.favouriteTips
            .deleteMany(
                Filters.`in`(
                    FavouriteTip::tipId.name,
                    tipIds
                )
            )
    }
}