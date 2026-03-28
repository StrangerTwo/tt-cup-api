package cz.ttcup.player.schema

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import cz.ttcup.player.model.Player

const val PLAYERS = "players"
val MongoDatabase.players
    get() = this.getCollection<Player>(PLAYERS)