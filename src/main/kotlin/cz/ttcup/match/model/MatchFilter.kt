package cz.ttcup.match.model

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Field
import com.mongodb.client.model.Filters
import cz.ttcup.database.util.*
import cz.ttcup.player.model.Player
import cz.ttcup.summary.model.Summary
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.conversions.Bson
import kotlin.reflect.KProperty1

@Serializable
sealed interface MatchFilter {
    fun getLookups(): List<KProperty1<TopMatch, *>>
    fun toFilters(): List<Bson>
}

@Serializable
@SerialName("*")
object StarPlayerFilter : MatchFilter {
    override fun getLookups(): List<KProperty1<TopMatch, *>> {
        return listOf(TopMatch::players);
    }

    override fun toFilters(): List<Bson> {
        return listOf(
            Aggregates.match(
                "${TopMatch::players.fieldName}.${Player::favourite.fieldName}" eq true
            )
        )
    }
}

@Serializable
@SerialName("ELO")
data class EloDifferenceFilter(val value: Int) : MatchFilter {
    override fun getLookups(): List<KProperty1<TopMatch, *>> {
        return listOf(TopMatch::players);
    }

    override fun toFilters(): List<Bson> {
        return filterPlayerDifference(Player::elo.fieldName, value)
    }
}

@Serializable
@SerialName("RANK")
data class RankDifferenceFilter(val value: Int) : MatchFilter {
    override fun getLookups(): List<KProperty1<TopMatch, *>> {
        return listOf(TopMatch::players);
    }

    override fun toFilters(): List<Bson> {
        return filterPlayerDifference(Player::ranking.fieldName, value)
    }
}

@Serializable
@SerialName("BIL")
data class BilanceFilter(val value: Int) : MatchFilter {
    override fun getLookups(): List<KProperty1<TopMatch, *>> {
        return listOf(TopMatch::summary);
    }

    override fun toFilters(): List<Bson> {
        return listOf(
            Aggregates.addFields(
                Field(
                    "bilance",
                    max(
                        "$${TopMatch::summary.fieldName}.${Summary::currentWinStreak.fieldName}",
                        "$${TopMatch::summary.fieldName}.${Summary::currentLoseStreak.fieldName}"
                    )
                )
            ),
            Aggregates.match(
                "bilance" gte value
            )
        )
    }
}

@Serializable
@SerialName("L_WON")
object LastMatchStrongLostFilter : MatchFilter {
    override fun getLookups(): List<KProperty1<TopMatch, *>> {
        return listOf(TopMatch::summary);
    }

    override fun toFilters(): List<Bson> {
        val absoluteMatchesWonDiff = 4
        return listOf(
            Aggregates.match(
                Filters.or(
                    Filters.and(
                        "${TopMatch::summary.fieldName}.${Summary::currentWinStreak.fieldName}" gte 1,
                        expr(
                            gte(
                                "$${TopMatch::summary.fieldName}.${Summary::matchesLost.fieldName}",
                                add(
                                    "$${TopMatch::summary.fieldName}.${Summary::matchesWon.fieldName}",
                                    absoluteMatchesWonDiff
                                )
                            )
                        )
                    ),
                    Filters.and(
                        "${TopMatch::summary.fieldName}.${Summary::currentLoseStreak.fieldName}" gte 1,
                        expr(
                            gte(
                                "$${TopMatch::summary.fieldName}.${Summary::matchesWon.fieldName}",
                                add(
                                    "$${TopMatch::summary.fieldName}.${Summary::matchesLost.fieldName}",
                                    absoluteMatchesWonDiff
                                )
                            )
                        )
                    )
                )
            )
        );
    }
}


private fun filterPlayerDifference(fieldName: String, value: Int): List<Bson> = listOf(
    Aggregates.addFields(
        Field(
            "p0$fieldName",
            getField(
                fieldName,
                arrayElemAt("$${TopMatch::players.fieldName}", 0)
            )
        ),
        Field(
            "p1$fieldName",
            getField(
                fieldName,
                arrayElemAt("$${TopMatch::players.fieldName}", 1)
            )
        )
    ),
    Aggregates.addFields(
        Field(
            "${fieldName}Diff",
            abs(
                subtract("\$p0$fieldName", "\$p1$fieldName")
            )
        )
    ),
    Aggregates.match(
        "${fieldName}Diff" gte value
    )
)