package cz.ttcup.admin.schema

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import cz.ttcup.admin.model.User

const val ADMINS = "admins"
val MongoDatabase.admins
    get() = this.getCollection<User>(ADMINS)
