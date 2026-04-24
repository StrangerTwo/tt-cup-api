package cz.ttcup.bet.schema

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import cz.ttcup.bet.model.Bet

const val BETS = "bets"
val MongoDatabase.bets
    get() = this.getCollection<Bet>(BETS)