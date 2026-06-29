package cz.ttcup.xg.tips.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.xg.tips.model.XgTip
import cz.ttcup.xg.tips.schema.xgTips
import kotlinx.coroutines.flow.toList

suspend fun getTips(): List<XgTip> {
    withDatabase {
        return database.xgTips
            .find()
            .toList()
    }
}