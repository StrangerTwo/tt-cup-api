package cz.ttcup.place.schema

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import cz.ttcup.place.model.Place

const val PLACES = "places"
val MongoDatabase.places
    get() = this.getCollection<Place>(PLACES)