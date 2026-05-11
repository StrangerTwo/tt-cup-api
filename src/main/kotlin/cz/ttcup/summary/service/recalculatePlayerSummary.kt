package cz.ttcup.summary.service

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOneModel
import com.mongodb.client.model.UpdateOptions
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.exists
import cz.ttcup.database.util.gte
import cz.ttcup.logs.service.logger
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.matches
import cz.ttcup.match.service.calculateMatchesSummary
import cz.ttcup.summary.model.Summary
import cz.ttcup.summary.schema.summaries
import kotlinx.coroutines.flow.toList
import java.time.LocalDateTime

suspend fun recalculatePlayerSummary(playerUid: String) {
    withDatabase {
        val matches = database.matches
            .find(
                Filters.and(
                    Match::playerUids eq playerUid,
                    Match::time gte LocalDateTime.now().minusYears(1),
                )
            )
            .toList()

        val opponentMatches = matches.groupBy { it.playerUids.first { rivalUid -> rivalUid != playerUid } }

        logger.info("Updated summaries. $playerUid, matches: ${matches.size}, opponents: ${opponentMatches.size}")

        database.summaries.deleteMany(Summary::playerUid eq playerUid)
        database.summaries
            .bulkWrite(
                listOf(
                    calculateMatchesSummary(matches, playerUid).let { summary ->
                        UpdateOneModel(
                            Filters.and(
                                Summary::playerUid eq playerUid,
                                Summary::rivalUid.exists(false)
                            ),
                            Summary(playerUid, null, summary).toUpdateBson(),
                            UpdateOptions().upsert(true)
                        )
                    },
                    *opponentMatches.map { (rivalUid, matches) ->
                        val summary = calculateMatchesSummary(matches, playerUid)
                        UpdateOneModel<Summary>(
                            Filters.and(
                                Summary::playerUid eq playerUid,
                                Summary::rivalUid eq rivalUid,
                            ),
                            Summary(playerUid, rivalUid, summary).toUpdateBson(),
                            UpdateOptions().upsert(true)
                        )
                    }.toTypedArray()
                )
            )
    }
}