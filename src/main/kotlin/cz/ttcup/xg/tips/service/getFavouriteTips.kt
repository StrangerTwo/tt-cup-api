package cz.ttcup.xg.tips.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.xg.tips.model.FavouriteTip
import cz.ttcup.xg.tips.schema.favouriteTips
import kotlinx.coroutines.flow.toList

suspend fun getFavouriteTips(userId: String): List<FavouriteTip> {
    withDatabase {
        return database.favouriteTips
            .find(FavouriteTip::userId eq userId)
            .toList()
    }
}