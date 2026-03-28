package cz.ttcup.admin.service

import cz.ttcup.admin.model.AuthPrincipal
import cz.ttcup.firebase.firebase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import java.io.File

fun Application.configureSecurity() {
    install(Authentication.Companion) {
        firebase {
            adminFile = File("admin.json")
            realm = "Bet tips"

            validate { token ->
                AuthPrincipal(token)
            }
        }
    }
}