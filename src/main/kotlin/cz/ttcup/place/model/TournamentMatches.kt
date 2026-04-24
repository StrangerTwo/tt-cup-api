package cz.ttcup.place.model

import cz.ttcup.match.model.Match
import cz.ttcup.tournament.model.Tournament
import kotlinx.serialization.Serializable

@Serializable
data class TournamentMatches(
    val tournament: Tournament,
    val matches: List<Match>,
)