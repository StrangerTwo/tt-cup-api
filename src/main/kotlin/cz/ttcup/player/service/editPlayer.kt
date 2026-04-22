package cz.ttcup.player.service

import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import cz.ttcup.context.service.withDatabase
import cz.ttcup.crawl.model.CrawlPage
import cz.ttcup.crawl.service.saveCrawlPages
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set
import cz.ttcup.player.model.EditPlayer
import cz.ttcup.player.model.Player
import cz.ttcup.player.schema.players

suspend fun editPlayer(player: Player, request: EditPlayer): UpdateResult {
    withDatabase {
        if (request.stisId != null && player.stisId != request.stisId) {
            saveCrawlPages(
                listOf(
                    CrawlPage("https://stis.ping-pong.cz/hrac-${request.stisId}")
                )
            )
        }


        return database.players
            .updateOne(
                Player::uid eq player.uid,
                Updates.combine(
                    Player::location set request.location,
                    Player::note set request.note,
                    Player::stisId set request.stisId,
                )
            )
    }
}