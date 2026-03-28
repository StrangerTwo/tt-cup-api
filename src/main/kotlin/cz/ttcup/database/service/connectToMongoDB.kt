package cz.ttcup.database.service

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.ktor.server.application.*

/**
 * Establishes connection with a MongoDB database.
 *
 * The following configuration properties (in application.yaml/application.conf) can be specified:
 * * `db.mongo.user` username for your database
 * * `db.mongo.password` password for the user
 * * `db.mongo.host` host that will be used for the database connection
 * * `db.mongo.port` port that will be used for the database connection
 * * `db.mongo.maxPoolSize` maximum number of connections to a MongoDB server
 * * `db.mongo.database.name` name of the database
 *
 * IMPORTANT NOTE: in order to make MongoDB connection working, you have to start a MongoDB server first.
 * See the instructions here: https://www.mongodb.com/docs/manual/administration/install-community/
 * all the parameters above
 *
 * @returns [com.mongodb.kotlin.client.coroutine.MongoDatabase] instance
 * */
internal fun Application.connectToMongoDB(): MongoClient {
    val user = System.getenv("DB_MONGO_USER")
    val password = System.getenv("DB_MONGO_PASSWORD")
    val host = System.getenv("DB_MONGO_HOST") ?: "127.0.0.1"
    val port = System.getenv("DB_MONGO_PORT") ?: "27017"
    val maxPoolSize = System.getenv("DB_MONGO_MAX_POOL_SIZE")?.toInt() ?: 20

    val credentials = user?.let { userVal -> password?.let { passwordVal -> "$userVal:$passwordVal@" } }.orEmpty()
    val uri = "mongodb://$credentials$host:$port/?maxPoolSize=$maxPoolSize&w=majority"

    val mongoClient = MongoClient.Factory.create(
        settings = MongoClientSettings
            .builder()
            .applyConnectionString(ConnectionString(uri))
            .build()
    )

    environment.monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }

    return mongoClient;
}