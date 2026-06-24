package cz.ttcup.behavior.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.gte
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.schema.tournaments
import kotlinx.coroutines.flow.toList
import java.time.LocalDate

suspend fun recalculatePlayerBehavior(playerUid: String) {
    withDatabase {
        val tournaments = database.tournaments
            .find(
                Filters.and(
                    Tournament::date gte LocalDate.now().minusYears(1),
                    Tournament::playersUids eq playerUid,
                )
            )
            .toList()
    }
}