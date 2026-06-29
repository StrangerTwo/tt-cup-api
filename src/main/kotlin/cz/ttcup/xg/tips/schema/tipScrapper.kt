package cz.ttcup.xg.tips.schema

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import cz.ttcup.xg.tips.model.TipScrapperLog

const val TIP_SCRAPPER = "tip-scrapper"
val MongoDatabase.tipScrapper
    get() = this.getCollection<TipScrapperLog>(TIP_SCRAPPER)