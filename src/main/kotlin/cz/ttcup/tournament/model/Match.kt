package cz.ttcup.tournament.model

import cz.ttcup.util.serializer.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Match(
    @Serializable(with = LocalDateTimeSerializer::class)
    val time: LocalDateTime,
    val playerUids: List<String>,
    var sets: List<Int>? = null,
    var points: List<String>? = null,
)