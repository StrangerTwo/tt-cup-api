package cz.ttcup.place.model

import cz.ttcup.player.model.Location
import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaceByTournament(
    val tournamentId: String,
    val location: Location? = null,
)