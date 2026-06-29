package cz.ttcup.xg.tips.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.xg.tips.model.TipScrapperLog
import cz.ttcup.xg.tips.schema.tipScrapper
import io.ktor.server.plugins.*
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId
import java.time.LocalDateTime

suspend fun acquireTipScrapperLock(): TipScrapperLog {
    withDatabase {
        database.tipScrapper
            .find(TipScrapperLog::end eq null)
            .firstOrNull()?.let { throw BadRequestException("Tip scrapper is already running") }

        val tipScrapperLog = TipScrapperLog(ObjectId(), LocalDateTime.now())
        database.tipScrapper.insertOne(tipScrapperLog);

        return tipScrapperLog;
    }
}