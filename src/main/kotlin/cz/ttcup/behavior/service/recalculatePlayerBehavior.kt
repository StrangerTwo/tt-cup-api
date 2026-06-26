package cz.ttcup.behavior.service

import com.mongodb.client.model.Filters
import cz.ttcup.behavior.model.Behavior
import cz.ttcup.behavior.model.PlayerBehavior
import cz.ttcup.behavior.model.PlayerBehaviorItem
import cz.ttcup.behavior.model.PlayerBehaviorType
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.asc
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.gte
import cz.ttcup.database.util.`in`
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.matches
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.schema.tournaments
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import java.time.LocalDate

suspend fun recalculatePlayerBehavior(playerUid: String) {
    withDatabase {
        val tournaments = database.tournaments
            .find(
                Filters.and(
                    Tournament::done eq true,
                    Tournament::date gte LocalDate.now().minusYears(1),
                    Tournament::playersUids eq playerUid,
                )
            )
            .toList()

        val matches = database.matches
            .find(Match::tournamentId `in` tournaments.map { it.objectId })
            .sort(Match::time.asc())
            .toList()
            .groupBy { it.tournamentId }


        val behavior = Behavior(
            playerUid = playerUid,
            behaviors = PlayerBehaviorType.entries.map { type ->
                PlayerBehavior(
                    type = type,
                    items = calculatePlayerBehaviorForType(playerUid, matches, type),
                )
            },
            ObjectId()
        )

        saveBehavior(behavior)
    }
}


fun calculatePlayerBehaviorForType(
    playerUid: String,
    matches: Map<ObjectId, List<Match>>,
    type: PlayerBehaviorType
): List<PlayerBehaviorItem?> {
    val tournamentBehaviors = matches.values.map { type.calculateTournamentBehavior(playerUid, it) }

    val maxSize = tournamentBehaviors.maxOfOrNull { it.size } ?: 0

    return List(maxSize) { i ->
        var sum = PlayerBehaviorItem()
        tournamentBehaviors.forEach {
            sum += it.getOrNull(i) ?: PlayerBehaviorItem()
        }
        sum
    }
}
