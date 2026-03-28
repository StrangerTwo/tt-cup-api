package cz.ttcup.context.service

import cz.ttcup.context.model.Ctx
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.*
import io.ktor.util.pipeline.*

val key = AttributeKey<Ctx>("ctx")
val RoutingContext.ctx: Ctx
    get() = this.call.attributes[key]

val PipelineContext<*, PipelineCall>.ctx: Ctx
    get() = this.call.attributes[key]