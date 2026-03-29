package cz.ttcup.tournament.model

import cz.ttcup.util.serializer.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Tournament(
    @Serializable(with = LocalDateSerializer::class)
    val date: LocalDate,
    val place: String,
    val matches: List<Match>
)