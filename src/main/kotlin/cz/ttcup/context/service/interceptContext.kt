package cz.ttcup.context.service

import cz.ttcup.admin.model.AuthPrincipal
import cz.ttcup.context.model.Ctx
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.interceptContext() {
    intercept(ApplicationCallPipeline.ApplicationPhase.Call) {
        val principal = this.call.principal<AuthPrincipal>()
        val ctx = Ctx(application, principal);
        call.attributes.put(key, ctx);
    }
}