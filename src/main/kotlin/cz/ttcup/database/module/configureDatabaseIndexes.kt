package cz.ttcup.database.module

import com.mongodb.client.model.Indexes
import cz.ttcup.bet.model.Bet
import cz.ttcup.bet.schema.bets
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.fieldName
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.matches
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.places
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players
import cz.ttcup.summary.model.Summary
import cz.ttcup.summary.schema.summaries
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.schema.tournaments
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking

fun Application.configureDatabaseIndexes() {
    runBlocking {
        withDatabase {
            database.matches
                .createIndex(Indexes.ascending(Match::tournamentId.fieldName))
            database.matches
                .createIndex(Indexes.ascending(Match::playerUids.fieldName))
            database.matches
                .createIndex(Indexes.ascending(Match::time.fieldName))
            database.places
                .createIndex(Indexes.ascending(Place::uid.fieldName))
            database.players
                .createIndex(Indexes.ascending(Player::uid.fieldName))
            database.tournaments
                .createIndex(Indexes.descending(Tournament::date.fieldName))
            database.tournaments
                .createIndex(Indexes.descending(Tournament::done.fieldName, Tournament::date.fieldName))
            database.tournaments
                .createIndex(Indexes.ascending(Tournament::place.fieldName))
            database.bets
                .createIndex(Indexes.ascending(Bet::matchId.fieldName))
            database.bets
                .createIndex(Indexes.ascending(Bet::tournamentId.fieldName))
            database.bets
                .createIndex(Indexes.ascending(Bet::playerUids.fieldName))
            database.summaries
                .createIndex(Indexes.ascending(Summary::playerUid.fieldName, Summary::rivalUid.fieldName))
            database.summaries
                .createIndex(Indexes.descending(Summary::winRate.fieldName))
        }
    }
}