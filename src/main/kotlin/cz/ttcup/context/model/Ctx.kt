package cz.ttcup.context.model

import cz.ttcup.admin.model.AuthPrincipal
import cz.ttcup.admin.model.User
import io.ktor.server.application.*

data class Ctx(
    val application: Application,

    val principal: AuthPrincipal? = null,
) {
    val user: User?
        get() = principal?.let { User(it.uid) }
}