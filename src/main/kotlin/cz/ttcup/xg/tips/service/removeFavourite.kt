package cz.ttcup.xg.tips.service

import com.mongodb.client.model.Filters
import com.mongodb.client.result.DeleteResult
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.xg.tips.model.FavouriteTip
import cz.ttcup.xg.tips.schema.favouriteTips

suspend fun removeFavourite(userId: String, tipId: String): DeleteResult {
    withDatabase {
        return database.favouriteTips
            .deleteOne(
                Filters.and(
                    FavouriteTip::userId eq userId,
                    FavouriteTip::tipId eq tipId
                )
            )
    }
}