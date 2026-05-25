package cz.ttcup.match.service

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Projections
import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.schema.BETS
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.fieldName
import cz.ttcup.database.util.lookupFirst
import cz.ttcup.database.util.ne
import cz.ttcup.match.model.GetTopMatchesRequest
import cz.ttcup.match.model.Match
import cz.ttcup.match.model.TopMatch
import cz.ttcup.match.model.TopMatches
import cz.ttcup.match.schema.MATCHES
import cz.ttcup.player.service.getPlayers
import cz.ttcup.summary.service.getSummaries
import cz.ttcup.util.service.queryPaginated

suspend fun getTopMatches(request: GetTopMatchesRequest): TopMatches {
    withDatabase {
        val (results, pageInfo) = database.getCollection<TopMatch>(MATCHES)
            .queryPaginated(
                request.pageInfo,
                preFilterAggregate = listOfNotNull(
                    request.done?.let {
                        Aggregates.match(
                            if (it) Match::sets eq 3
                            else Match::sets ne 3
                        )
                    },
                    Aggregates.project(
                        Projections.computed(TopMatch::match.fieldName, "\$\$ROOT")
                    ),
                ),
                resultFacetPipeline = listOf(
                    *lookupFirst(
                        BETS,
                        "${TopMatch::match.fieldName}.${Match::objectId.fieldName}",
                        Bet::matchId.fieldName,
                        TopMatch::bet.fieldName,
                    ),
                )
            );

        val players = results
            .map { it.match }
            .flatMap { it.playerUids }
            .distinct()
            .takeIf { it.isNotEmpty() }
            ?.let { getPlayers(it) } ?: listOf()
        val summaries = results
            .map { it.match }
            .map { it.playerUids[0] to it.playerUids[1] }
            .distinct()
            .takeIf { it.isNotEmpty() }
            ?.let { getSummaries(it) } ?: listOf()

        return TopMatches(results, pageInfo, players, summaries)
    }
}

