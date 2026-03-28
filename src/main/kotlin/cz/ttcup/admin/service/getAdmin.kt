package cz.ttcup.admin.service

import cz.ttcup.admin.model.AuthPrincipal
import cz.ttcup.admin.model.User
import cz.ttcup.admin.schema.admins
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import kotlinx.coroutines.flow.firstOrNull

suspend fun getAdmin(principal: AuthPrincipal): User? {
    withDatabase {
        return database
            .admins
            .find()
            .filter(User::uid eq principal.uid)
            .firstOrNull()
    }
}