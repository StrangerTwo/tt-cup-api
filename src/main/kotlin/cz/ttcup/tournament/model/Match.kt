package cz.ttcup.tournament.model

import cz.ttcup.util.serializer.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class Match(
    @Serializable(with = LocalTimeSerializer::class)
    val time: LocalTime,
    val playerUids: List<String>,
    var sets: List<Int>? = null,
    var points: List<String>? = null,
)