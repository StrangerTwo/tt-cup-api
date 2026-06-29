package cz.ttcup.xg.tips.service

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.xg.tips.model.FavouriteTip
import cz.ttcup.xg.tips.model.UpdateFavouriteTip
import cz.ttcup.xg.tips.schema.favouriteTips
import java.time.LocalDateTime

suspend fun setFavourite(userId: String, tipId: String, request: UpdateFavouriteTip) {
    withDatabase {
        database.favouriteTips
            .updateOne(
                Filters.and(
                    FavouriteTip::userId eq userId,
                    FavouriteTip::tipId eq tipId,
                ),
                Updates.combine(
                    FavouriteTip::timestamp set LocalDateTime.now(),
                    FavouriteTip::tickets set request.tickets,
                ),
                UpdateOptions().upsert(true)
            )
    }
}