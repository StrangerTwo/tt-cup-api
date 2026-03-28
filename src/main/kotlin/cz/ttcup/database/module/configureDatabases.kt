package cz.ttcup.database.module

import com.mongodb.kotlin.client.coroutine.MongoClient
import cz.ttcup.database.service.connectToMongoDB
import io.ktor.server.application.*

@PublishedApi
internal lateinit var mongoClient: MongoClient;

fun Application.configureDatabases() {
    mongoClient = connectToMongoDB()
}

