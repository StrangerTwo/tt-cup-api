package cz.ttcup.behavior.service

import com.mongodb.client.model.Filters
import cz.ttcup.behavior.model.PlayerBehaviorType
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.asc
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.gte
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.matches
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.schema.tournaments
import kotlinx.coroutines.flow.toList
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

        val behaviors = tournaments.map { tournament ->
            val matches = database.matches
                .find(Match::tournamentId eq tournament.objectId)
                .sort(Match::time.asc())
                .toList()
            if (matches.size != 8) return@map null
            val playerMatches = matches.filter { it.playerUids.contains(playerUid) }
            if (playerMatches.size != 4) return@map null

            val result = PlayerBehaviorType.entries.associateWith {
                it.instance.calculateTournamentBehavior(playerUid, matches).toList()
            }

            return@map result
        }

    }
}
