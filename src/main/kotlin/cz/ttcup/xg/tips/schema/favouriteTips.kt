package cz.ttcup.xg.tips.schema

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import cz.ttcup.xg.tips.model.FavouriteTip

const val FAVOURITE_TIPS = "favourite-tips"
val MongoDatabase.favouriteTips
    get() = this.getCollection<FavouriteTip>(FAVOURITE_TIPS)