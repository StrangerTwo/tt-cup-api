package cz.ttcup.match.service

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
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
import org.bson.Document

suspend fun getRivalMatches(
    request: GetPlayerRivalsRequest,
    player: Player
): PaginatedQueryResult<RivalMatches> = withDatabase {
    database.getCollection<RivalMatches>(MATCHES)
        .queryPaginated(
            request.pageInfo,
            aggregate = listOf(
                Aggregates.match(Match::playerUids eq player.uid),
                Aggregates.unwind("$${Match::playerUids.fieldName}"),
                Aggregates.match(Match::playerUids ne player.uid),
                Aggregates.group("$${Match::playerUids.fieldName}"),
                Aggregates.lookup(
                    PLAYERS,
                    "_id",
                    Player::uid.fieldName,
                    "rival"
                ),
                Aggregates.project(
                    Projections.computed("rival", Projections.computed("\$first", "\$rival"))
                ),
            ),
            filter = listOfNotNull(
                request.search?.let { Player::name contains it },
                request.location?.let { Player::location eq it },
            ).takeIf {
                it.isNotEmpty()
            }?.let {
                Aggregates.match(Filters.and(it))
            },
            postSortAggregate = listOf(
                Document(
                    "\$lookup",
                    Document(
                        mapOf(
                            "from" to MATCHES,
                            "localField" to "rival.uid",
                            "foreignField" to Match::playerUids.fieldName,
                            "as" to "matches",
                            "pipeline" to listOf(
                                Aggregates.match(Match::playerUids eq player.uid),
                                Aggregates.sort(Match::time.asc())
                            )
                        )
                    )
                )
            )
        )
}