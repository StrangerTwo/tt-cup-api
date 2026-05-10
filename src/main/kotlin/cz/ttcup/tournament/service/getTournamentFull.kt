package cz.ttcup.tournament.service

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.schema.BETS
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.*
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.MATCHES
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.PLACES
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.PLAYERS
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.model.TournamentFull
import cz.ttcup.tournament.schema.tournaments
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId
import java.time.LocalDate

suspend fun getTournamentFull(
    tournamentId: String? = null,
    place: String? = null,
    date: LocalDate? = null,
    players: Boolean = false,
): TournamentFull? {
    withDatabase {
        return database.tournaments
            .aggregate<TournamentFull>(
                listOfNotNull(
                    listOfNotNull(
                        tournamentId?.let { Tournament::objectId eq ObjectId(it) },
                        place?.let { Tournament::place eq it },
                        date?.let { Tournament::date eq it },
                    ).takeIf {
                        it.isNotEmpty()
                    }?.let {
                        Aggregates.match(
                            Filters.and(it)
                        )
                    },
                    Aggregates.project(
                        Projections.computed(TournamentFull::tournament.fieldName, "\$\$ROOT")
                    ),
                    *lookupFirst(
                        PLACES,
                        "tournament.${Tournament::place.fieldName}",
                        Place::uid.fieldName,
                        TournamentFull::place.fieldName,
                    ),
                    lookup(
                        MATCHES,
                        "tournament.${Tournament::objectId.fieldName}",
                        Match::tournamentId.fieldName,
                        TournamentFull::matches.fieldName,
                        listOf(
                            Aggregates.sort(Match::time.asc())
                        )
                    ),
                    lookup(
                        BETS,
                        "tournament.${Tournament::objectId.fieldName}",
                        Bet::tournamentId.fieldName,
                        TournamentFull::bets.fieldName,
                    ),
                    players.takeIf { it }?.let {
                        lookup(
                            PLAYERS,
                            "${TournamentFull::matches.fieldName}.${Match::playerUids.fieldName}",
                            Player::uid.fieldName,
                            TournamentFull::players.fieldName,
                        )
                    }
                )
            )
            .firstOrNull()
    }
}