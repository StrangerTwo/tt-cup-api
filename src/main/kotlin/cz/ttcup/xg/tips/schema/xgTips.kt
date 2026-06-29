package cz.ttcup.xg.tips.schema

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import cz.ttcup.xg.tips.model.XgTip

const val XG_TIPS = "xgTips"
val MongoDatabase.xgTips
    get() = this.getCollection<XgTip>(XG_TIPS)