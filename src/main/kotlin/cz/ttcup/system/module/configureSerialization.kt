package cz.ttcup.system.module

import cz.ttcup.util.serializer.LocalDateSerializer
import cz.ttcup.util.serializer.LocalDateTimeSerializer
import cz.ttcup.util.serializer.LocalTimeSerializer
import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.kotlinx.xml.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import nl.adaptivity.xmlutil.serialization.XML
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        xml(
            XML(
                serializersModule = SerializersModule {
                    contextual(LocalDate::class, LocalDateSerializer)
                    contextual(LocalDateTime::class, LocalDateTimeSerializer)
                    contextual(LocalTime::class, LocalTimeSerializer)
                },
            )
        )
        json(Json {
            serializersModule = SerializersModule {
                contextual(LocalDate::class, LocalDateSerializer)
                contextual(LocalDateTime::class, LocalDateTimeSerializer)
                contextual(LocalTime::class, LocalTimeSerializer)
            }
        })
    }
}

@OptIn(InternalAPI::class)
val defaultSerializationPlugin = createRouteScopedPlugin("serialization") {
    onCall { call ->
        if (call.request.accept()?.contains("text/html") == true) {
            call.request.setHeader("Accept", listOf("application/json"))
        }
    }
}