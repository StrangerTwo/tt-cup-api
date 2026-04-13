package cz.ttcup.match.service

import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.match.model.Match
import cz.ttcup.match.schema.matches
import cz.ttcup.player.model.Player
import kotlinx.coroutines.flow.toList

suspend fun getPlayerMatches(player: Player): List<Match> {
    withDatabase {
        return database.matches
            .find(
                Match::playerUids eq player.uid,
            )
            .toList()
    }
}