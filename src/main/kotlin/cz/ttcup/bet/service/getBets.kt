package cz.ttcup.bet.service

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.model.BetFull
import cz.ttcup.bet.model.BetType
import cz.ttcup.bet.model.GetBetsRequest
import cz.ttcup.bet.schema.BETS
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.*
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.MATCHES
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated

suspend fun getBets(request: GetBetsRequest): PaginatedQueryResult<BetFull> {
    withDatabase {
        return database.getCollection<BetFull>(BETS)
            .queryPaginated(
                request.pageInfo,
                listOfNotNull(
                    request.location?.let { Bet::location eq it },
                    request.tournamentId?.let { Bet::tournamentId eq it },
                    request.playerUids?.let { if (it.size == 2) Bet::playerUids all it else Bet::playerUids eq it.first() },
                    request.matchId?.let { Bet::matchId eq it },
                    request.type
                        ?.let { Bet::type eq it }
                        ?: (Bet::type ne BetType.NO_BET),
                    request.win?.let { Bet::win eq it },
                    request.resolved?.let { if (it) Bet::win ne null else Bet::win eq null },
                ).takeIf {
                    it.isNotEmpty()
                }?.let {
                    Filters.and(it)
                },
                resultFacetPipeline = listOf(
                    Aggregates.project(
                        Projections.computed(BetFull::bet.fieldName, "\$\$ROOT")
                    ),
                    *lookupFirst(
                        MATCHES,
                        "bet.${Bet::matchId.fieldName}",
                        Match::objectId.fieldName,
                        BetFull::match.fieldName,
                    )
                ),
            )
    }
}

