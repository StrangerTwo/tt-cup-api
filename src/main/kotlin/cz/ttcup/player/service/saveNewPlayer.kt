package cz.ttcup.player.service

import com.mongodb.client.result.InsertOneResult
import cz.ttcup.context.service.withDatabase
import cz.ttcup.player.model.NewPlayer
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players

suspend fun saveNewPlayer(player: NewPlayer): InsertOneResult {
    withDatabase {
        val existingPlayer = getPlayer(player.uid)
        if (existingPlayer != null)
            throw IllegalArgumentException("Player ${existingPlayer.name} (${player.uid}) already exists")

        return database.players
            .insertOne(
                Player(
                    uid = player.uid,
                )
            )
    }
}