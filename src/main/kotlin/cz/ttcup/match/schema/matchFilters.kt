package cz.ttcup.match.schema

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import cz.ttcup.match.model.MatchFilter

const val MATCH_FILTERS = "match-filters"
val MongoDatabase.matchFilters
    get() = this.getCollection<MatchFilter>(MATCH_FILTERS)