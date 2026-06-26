package cz.ttcup.behavior.schema

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import cz.ttcup.behavior.model.Behavior

const val BEHAVIORS = "behaviors"
val MongoDatabase.behaviors
    get() = this.getCollection<Behavior>(BEHAVIORS)

