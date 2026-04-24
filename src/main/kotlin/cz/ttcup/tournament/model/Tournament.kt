package cz.ttcup.tournament.model

import cz.ttcup.util.serializer.LocalDateSerializer
import cz.ttcup.util.serializer.ObjectIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import java.time.LocalDate

@Serializable
data class Tournament(
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val place: String,

    @SerialName("_id")
    @Serializable(with = ObjectIdSerializer::class)
    val objectId: ObjectId
)