package cz.ttcup.xg.tips.service

import com.mongodb.client.result.UpdateResult
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.xg.tips.model.TipScrapperLog
import cz.ttcup.xg.tips.schema.tipScrapper
import java.time.LocalDateTime

suspend fun releaseTipScrapperLock(lock: TipScrapperLog): UpdateResult {
    withDatabase {
        return database.tipScrapper
            .updateOne(
                TipScrapperLog::objectId eq lock.objectId,
                TipScrapperLog::end set LocalDateTime.now(),
            )
    }
}