package cz.ttcup.tournament.model

import cz.ttcup.match.model.Match
import cz.ttcup.util.serializer.LocalDateSerializer
import cz.ttcup.util.serializer.ObjectIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import java.time.LocalDate

@Serializable
data class TournamentFull(
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val place: String,
    val matches: List<Match>,

    @SerialName("_id")
    @Serializable(with = ObjectIdSerializer::class)
    val objectId: ObjectId,
)