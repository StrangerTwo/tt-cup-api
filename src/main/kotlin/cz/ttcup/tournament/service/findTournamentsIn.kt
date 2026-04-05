package cz.ttcup.tournament.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.schema.tournaments
import kotlinx.coroutines.flow.toList
import java.time.LocalDate

suspend fun findTournamentsIn(crawlPages: List<Pair<LocalDate, String>>): List<Tournament> {
    withDatabase {
        return database.tournaments
            .find(
                Filters.or(
                    crawlPages.map { (date, place) ->
                        Filters.and(
                            Tournament::date eq date,
                            Tournament::place eq place,
                        )
                    }
                )
            )
            .toList()
    }
}