package cz.ttcup.tournament.model

import cz.ttcup.bet.model.Bet
import cz.ttcup.match.model.Match
import cz.ttcup.place.model.Place
import kotlinx.serialization.Serializable

@Serializable
data class TournamentFull(
    val place: Place? = null,
    val tournament: Tournament,
    val matches: List<Match>,
    val bets: List<Bet>,
)