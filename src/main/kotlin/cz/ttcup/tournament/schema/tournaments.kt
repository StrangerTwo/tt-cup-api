package cz.ttcup.tournament.schema

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import cz.ttcup.tournament.model.Tournament

const val TOURNAMENTS = "tournaments"
val MongoDatabase.tournaments
    get() = this.getCollection<Tournament>(TOURNAMENTS)