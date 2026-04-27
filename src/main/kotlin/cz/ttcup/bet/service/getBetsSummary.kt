package cz.ttcup.bet.service

import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.model.BetSummary
import cz.ttcup.bet.schema.BETS
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.fieldName
import cz.ttcup.player.model.Location
import kotlinx.coroutines.flow.toList

suspend fun getBetsSummary(location: Location?): BetSummary {
    withDatabase {
        val betWinCounts = database.getCollection<BetWinCount>(BETS)
            .aggregate(
                listOfNotNull(
                    listOfNotNull(
                        location?.let { Bet::location eq it }
                    ).takeIf {
                        it.isNotEmpty()
                    }?.let {
                        Aggregates.match(Filters.and(it))
                    },
                    Aggregates.group(
                        "$${Bet::win.fieldName}",
                        Accumulators.sum(BetWinCount::count.fieldName, 1)
                    )
                )
            )
            .toList()

        return BetSummary(
            betsWon = betWinCounts.find { it._id == true }?.count ?: 0,
            betsLost = betWinCounts.find { it._id == false }?.count ?: 0,
            betsUnresolved = betWinCounts.find { it._id == null }?.count ?: 0,
        )
    }
}

data class BetWinCount(
    val _id: Boolean?,
    val count: Int
)