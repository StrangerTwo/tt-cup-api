package cz.ttcup.system.module

import cz.ttcup.util.serializer.LocalDateSerializer
import cz.ttcup.util.serializer.LocalDateTimeSerializer
import cz.ttcup.util.serializer.LocalTimeSerializer
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalSerializationApi::class)
val ktorClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            explicitNulls = false
            serializersModule = SerializersModule {
                contextual(LocalDate::class, LocalDateSerializer)
                contextual(LocalDateTime::class, LocalDateTimeSerializer)
                contextual(LocalTime::class, LocalTimeSerializer)
            }
        })
    }
}