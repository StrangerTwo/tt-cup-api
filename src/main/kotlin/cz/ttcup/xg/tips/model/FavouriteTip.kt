package cz.ttcup.xg.tips.model

import cz.ttcup.util.serializer.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
class FavouriteTip(
    val userId: String,
    val tipId: String,
    val tickets: List<Int>? = null,

    @Serializable(with = LocalDateTimeSerializer::class)
    val timestamp: LocalDateTime,
)