package cz.ttcup.tournament.model

import cz.ttcup.bet.model.Bet
import cz.ttcup.match.model.Match
import cz.ttcup.place.model.Place
import kotlinx.serialization.Serializable

@Serializable
data class TournamentToday(
    val place: Place,
    val tournament: Tournament? = null,
    val matches: List<Match>,
    val bets: List<Bet>,
)