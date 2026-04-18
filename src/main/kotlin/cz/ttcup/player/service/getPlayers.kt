package cz.ttcup.player.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.contains
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.`in`
import cz.ttcup.player.model.GetPlayersRequest
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated
import kotlinx.coroutines.flow.toList

suspend fun getPlayers(uids: List<String>): List<Player> {
    withDatabase {
        return database.players
            .find(Player::uid `in` uids)
            .toList()
    }
}

suspend fun getPlayers(request: GetPlayersRequest): PaginatedQueryResult<Player> {
    withDatabase {
        return database.players
            .queryPaginated(
                request.pageInfo,
                listOfNotNull(
                    request.search?.let { Player::name contains it },
                    request.location?.let { Player::location eq it },
                ).takeIf {
                    it.isNotEmpty()
                }?.let {
                    Filters.and(it)
                }
            )
    }
}