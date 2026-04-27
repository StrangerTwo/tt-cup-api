package cz.ttcup.bet.service

import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.model.BetType
import cz.ttcup.bet.model.NewBet
import cz.ttcup.match.model.Match
import cz.ttcup.place.model.Place
import cz.ttcup.tournament.model.Tournament
import io.ktor.server.plugins.*
import org.bson.types.ObjectId

suspend fun setBet(
    match: Match,
    tournament: Tournament,
    place: Place,
    request: NewBet
): Bet {
    val bet = Bet(
        place.location,
        tournament.objectId,
        match.objectId,
        match.playerUids,
        request.type,
        Bet.isWin(request.type, match),
        ObjectId()
    )
    if (request.type == BetType.NO_BET)
        deleteBet(match.objectId)
            .let { if (it.deletedCount != 1L) throw NotFoundException("Bet not found") }
    else
        upsertBet(bet)

    return bet;
}