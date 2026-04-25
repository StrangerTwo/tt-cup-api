package cz.ttcup.database.module

import com.mongodb.client.model.Indexes
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.fieldName
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.matches
import io.ktor.server.application.*
import kotlinx.coroutines.launch

fun Application.configureDatabaseIndexes() {
    launch {
        withDatabase {
            database.matches
                .createIndex(Indexes.ascending(Match::tournamentId.fieldName))
        }
    }
}