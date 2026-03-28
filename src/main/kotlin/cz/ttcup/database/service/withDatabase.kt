package cz.ttcup.database.service

import cz.ttcup.context.model.DatabaseContext
import cz.ttcup.database.module.mongoClient

inline fun <T : Any> withDatabase(databaseName: String, body: DatabaseContext.() -> T): T {
    val database = mongoClient.getDatabase(databaseName);
    return body(DatabaseContext(database = database))
}