package cz.ttcup.capture.service

import cz.ttcup.logs.service.logger
import cz.ttcup.match.model.Match
import cz.ttcup.place.service.placeUpdated
import cz.ttcup.player.service.getPlayers
import cz.ttcup.summary.service.recalculatePlayerSummary
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.service.saveTournament
import cz.ttcup.util.toLocalTime
import org.bson.types.ObjectId
import org.jsoup.nodes.Element
import java.time.LocalDate
import java.time.LocalTime

suspend fun captureTournament(table: Element, date: LocalDate, place: String) {
    val tournamentId = ObjectId()
    val matches = table.select("tr:not(.table-date)").mapNotNull { row ->
        val time = row.child(1).text().trim().takeLast(5).toLocalTime("HH:mm")

        val vsLink = row.child(2).selectFirst("a")!!.attr("href")
        val (player1Uid, player2Uid) = ".+/([^\\/]+)/vs/([^\\/]+)".toRegex().find(vsLink)?.destructured
            ?: return@mapNotNull null

        val setsText = row.child(3).text().trim();
        val setsFind = "(\\d+):(\\d+)".toRegex().find(setsText)
        var sets: List<Int>? = null
        if (setsFind != null) {
            val (sets1, sets2) = setsFind.destructured;
            sets = listOf(sets1.toInt(), sets2.toInt())
        }

        val pointsText = row.child(4).text().trim();
        val pointFind = "(\\d+):(\\d+)".toRegex().findAll(pointsText).toList();
        var points: List<String>? = null
        if (pointFind.isNotEmpty()) {
            points = pointFind.map {
                val (points1, points2) = it.destructured;
                return@map "${points1.toInt()}:${points2.toInt()}"
            }
        }

        return@mapNotNull Match(
            date.atTime(time),
            listOf(player1Uid, player2Uid),
            sets, points, tournamentId, ObjectId()
        );
    }

    val players = getPlayers(matches.flatMap { it.playerUids })
    if (players.isEmpty()) return;

    val finalsShown = table.select("tr.table-date").any { it.text().contains("Final") }

    val isDone = date.isBefore(LocalDate.now().minusDays(1)) || matches.all { it.isDone } && finalsShown

    val tournament = Tournament(date, place, isDone, tournamentId);
    logger.info(
        "Saving tournament ${tournament.date}/${tournament.place} for players ${players.joinToString(", ") { it.uid }}"
    )

    saveTournament(tournament, matches.map {
        it.copy(time = if (it.time.isBefore(matches.first().time)) it.time.plusDays(1) else it.time)
    })
    placeUpdated(place, tournament.date.atTime(LocalTime.now()))
    players.forEach { recalculatePlayerSummary(it.uid) }
}

