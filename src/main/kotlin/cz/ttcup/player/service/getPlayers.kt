package cz.ttcup.player.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.contains
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
                request.search?.let { Player::name contains it }
            )
    }
}