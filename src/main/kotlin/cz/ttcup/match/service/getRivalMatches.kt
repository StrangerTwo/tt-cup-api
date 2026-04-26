package cz.ttcup.match.service

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.*
import cz.ttcup.match.model.Match
import cz.ttcup.match.model.RivalMatches
import cz.ttcup.match.schema.MATCHES
import cz.ttcup.player.model.GetPlayerRivalsRequest
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.PLAYERS
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated

suspend fun getRivalMatches(
    request: GetPlayerRivalsRequest,
    player: Player
): PaginatedQueryResult<RivalMatches> = withDatabase {
    database.getCollection<RivalMatches>(MATCHES)
        .queryPaginated(
            request.pageInfo,
            preFilterAggregate = listOf(
                Aggregates.match(Match::playerUids eq player.uid),
                Aggregates.unwind("$${Match::playerUids.fieldName}"),
                Aggregates.match(Match::playerUids ne player.uid),
                Aggregates.group("$${Match::playerUids.fieldName}"),
                *lookupFirst(
                    PLAYERS,
                    "_id",
                    Player::uid.fieldName,
                    "rival"
                ),
            ),
            filter = listOfNotNull(
                request.search?.let { "rival.${Player::name.fieldName}" contains it },
                request.location?.let { "rival.${Player::location.fieldName}" eq it },
            ).takeIf {
                it.isNotEmpty()
            }?.let {
                Filters.and(it)
            },
            postPaginatedAggregate = listOf(
                lookup(
                    MATCHES,
                    "rival.uid",
                    Match::playerUids.fieldName,
                    RivalMatches::matches.fieldName,
                    listOf(
                        Aggregates.match(Match::playerUids eq player.uid),
                        Aggregates.sort(Match::time.asc())
                    )
                )
            )
        )
}