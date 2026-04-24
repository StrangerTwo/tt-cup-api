package cz.ttcup.bet.service

import com.mongodb.client.model.Filters
import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.model.GetBetsRequest
import cz.ttcup.bet.schema.bets
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.all
import cz.ttcup.database.util.eq
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated

suspend fun getBets(request: GetBetsRequest): PaginatedQueryResult<Bet> {
    withDatabase {
        return database.bets
            .queryPaginated(
                request.pageInfo,
                listOfNotNull(
                    request.location?.let { Bet::location eq it },
                    request.tournamentId?.let { Bet::tournamentId eq it },
                    request.playerUids?.let { if (it.size == 2) Bet::playerUids all it else Bet::playerUids eq it.first() },
                    request.matchId?.let { Bet::matchId eq it },
                    request.type?.let { Bet::type eq it },
                ).takeIf {
                    it.isNotEmpty()
                }?.let {
                    Filters.and(it)
                }
            )
    }
}