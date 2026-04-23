package cz.ttcup.tournament.model

import cz.ttcup.match.model.Match
import cz.ttcup.place.model.Place
import kotlinx.serialization.Serializable

@Serializable
data class PlaceTournamentMatches(
    val place: Place,
    val tournament: Tournament? = null,
    val matches: List<Match>,
)