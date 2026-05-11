package cz.ttcup.summary.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.summary.model.Summary
import cz.ttcup.summary.schema.summaries
import kotlinx.coroutines.flow.toList

suspend fun getSummaries(uids: List<Pair<String, String>>): List<Summary> {
    withDatabase {
        return database.summaries
            .find(
                Filters.or(
                    uids.map { (playerUid, rivalUid) ->
                        Filters.and(
                            Summary::playerUid eq playerUid,
                            Summary::rivalUid eq rivalUid
                        )
                    }
                )
            )
            .toList()
    }
}