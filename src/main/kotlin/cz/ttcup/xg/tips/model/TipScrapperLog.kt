package cz.ttcup.xg.tips.model

import cz.ttcup.util.serializer.LocalDateTimeSerializer
import cz.ttcup.util.serializer.ObjectIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import java.time.LocalDateTime

@Serializable
data class TipScrapperLog(
    @SerialName("_id")
    @Serializable(with = ObjectIdSerializer::class)
    val objectId: ObjectId? = null,

    @Serializable(with = LocalDateTimeSerializer::class)
    val start: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val end: LocalDateTime? = null,
)