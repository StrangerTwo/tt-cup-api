package cz.ttcup.match.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.match.model.MatchFilter
import cz.ttcup.match.schema.matchFilters
import kotlinx.coroutines.flow.toList

suspend fun getMatchFilters(): List<MatchFilter> {
    withDatabase {
        return database.matchFilters
            .find()
            .toList()
    }
}