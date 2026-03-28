package cz.ttcup.admin.model

import cz.ttcup.firebase.FirebaseToken
import io.ktor.server.auth.*

data class AuthPrincipal(
    val uid: String,
    val name: String?,
) : Principal {
    constructor(token: FirebaseToken) : this(
        uid = token.uid,
        name = token.name,
    )
}