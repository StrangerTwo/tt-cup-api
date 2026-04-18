package cz.ttcup.match.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.asc
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.matches
import kotlinx.coroutines.flow.toList
import org.bson.BsonDocument
import org.bson.conversions.Bson

suspend fun getAllMatches(filter: Bson?): List<Match> = withDatabase {
    database.matches
        .find(filter ?: BsonDocument())
        .sort(Match::time.asc())
        .toList()
}