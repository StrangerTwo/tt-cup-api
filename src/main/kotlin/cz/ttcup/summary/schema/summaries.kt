package cz.ttcup.summary.schema

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import cz.ttcup.summary.model.Summary

const val SUMMARIES = "summaries"
val MongoDatabase.summaries
    get() = this.getCollection<Summary>(SUMMARIES)