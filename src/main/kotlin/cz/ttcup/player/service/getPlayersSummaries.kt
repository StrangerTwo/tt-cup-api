package cz.ttcup.player.service

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.*
import cz.ttcup.player.model.GetPlayersRequest
import cz.ttcup.player.model.Player
import cz.ttcup.player.model.PlayerSummary
import cz.ttcup.player.schema.PLAYERS
import cz.ttcup.summary.model.Summary
import cz.ttcup.summary.schema.SUMMARIES
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated

suspend fun getPlayersSummaries(request: GetPlayersRequest): PaginatedQueryResult<PlayerSummary> {
    withDatabase {
        return database.getCollection<PlayerSummary>(PLAYERS)
            .queryPaginated(
                request.pageInfo,
                preFilterAggregate = listOf(
                    Aggregates.project(
                        Projections.computed(PlayerSummary::player.fieldName, "\$\$ROOT")
                    ),
                    *lookupFirst(
                        SUMMARIES,
                        "player.${Player::uid.fieldName}",
                        Summary::playerUid.fieldName,
                        PlayerSummary::summary.fieldName,
                        listOf(
                            Aggregates.match(Summary::rivalUid.exists(false)),
                        )
                    ),
                ),
                filter = listOfNotNull(
                    request.search?.let { "player.${Player::name.fieldName}" contains it },
                    request.location?.let { "player.${Player::location.fieldName}" eq it },
                    request.uids?.let { "player.${Player::uid.fieldName}" `in` it },
                ).takeIf {
                    it.isNotEmpty()
                }?.let {
                    Filters.and(it)
                },
                logDebug = true
            )
    }
}