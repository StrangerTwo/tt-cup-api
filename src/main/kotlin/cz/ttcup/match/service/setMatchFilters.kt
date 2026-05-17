package cz.ttcup.match.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.match.model.SetTopMatchConfigRequest
import cz.ttcup.match.schema.matchFilters

suspend fun setMatchFilters(config: SetTopMatchConfigRequest) {
    withDatabase {
        database.matchFilters
            .deleteMany(Filters.empty())

        if (config.filters.isNotEmpty())
            database.matchFilters
                .insertMany(config.filters)
    }
}