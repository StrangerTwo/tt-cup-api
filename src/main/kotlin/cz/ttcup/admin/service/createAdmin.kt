package cz.ttcup.admin.service

import cz.ttcup.admin.model.User
import cz.ttcup.admin.schema.admins
import cz.ttcup.context.service.withDatabase

suspend fun createAdmin(user: User): User {
    withDatabase {
        database
            .admins
            .insertOne(user)
    }
    return user
}