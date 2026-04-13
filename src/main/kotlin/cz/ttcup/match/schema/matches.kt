package cz.ttcup.match.schema

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import cz.ttcup.match.model.Match

const val MATCHES = "matches"
val MongoDatabase.matches
    get() = this.getCollection<Match>(MATCHES)