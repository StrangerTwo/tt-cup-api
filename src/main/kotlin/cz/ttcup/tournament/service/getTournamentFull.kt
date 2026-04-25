package cz.ttcup.tournament.service

import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Field
import com.mongodb.client.model.Projections
import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.schema.BETS
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.asc
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.fieldName
import cz.ttcup.database.util.lookup
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.MATCHES
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.PLACES
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.model.TournamentFull
import cz.ttcup.tournament.schema.tournaments
import kotlinx.coroutines.flow.firstOrNull
import org.bson.Document
import org.bson.types.ObjectId

suspend fun getTournamentFull(tournamentId: String): TournamentFull? {
    withDatabase {
        return database.tournaments
            .aggregate<TournamentFull>(
                listOf(
                    Aggregates.match(
                        Tournament::objectId eq ObjectId(tournamentId)
                    ),
                    Aggregates.project(
                        Projections.computed(TournamentFull::tournament.fieldName, "\$\$ROOT")
                    ),
                    lookup(
                        PLACES,
                        "tournament.${Tournament::place.fieldName}",
                        Place::uid.fieldName,
                        TournamentFull::place.fieldName,
                    ),
                    Aggregates.addFields(
                        Field(
                            TournamentFull::place.fieldName,
                            Document("\$first", "$${TournamentFull::place.fieldName}")
                        )
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
                    )
                )
            )
            .firstOrNull()
    }
}