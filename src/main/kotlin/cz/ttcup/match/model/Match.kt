package cz.ttcup.match.model

import cz.ttcup.util.serializer.LocalDateTimeSerializer
import cz.ttcup.util.serializer.ObjectIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import java.time.LocalDateTime

@Serializable
data class Match(
    @Serializable(with = LocalDateTimeSerializer::class)
    val time: LocalDateTime,
    val playerUids: List<String>,
    val sets: List<Int>? = null,
    val points: List<String>? = null,

    @Serializable(with = ObjectIdSerializer::class)
    val tournamentId: ObjectId? = null,

    @SerialName("_id")
    @Serializable(with = ObjectIdSerializer::class)
    val objectId: ObjectId? = null
) {
    fun isWin(playerUid: String): Boolean? {
        return if (playerUids.first() == playerUid)
            sets?.let { it.first() > it.last() } else
            sets?.let { it.last() > it.first() }
    }
}