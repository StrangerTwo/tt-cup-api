package cz.ttcup.capture.service

import cz.ttcup.capture.model.CaptureDto
import cz.ttcup.logs.service.logger
import cz.ttcup.player.service.getPlayers
import cz.ttcup.tournament.model.Match
import cz.ttcup.tournament.model.Tournament
import cz.ttcup.tournament.service.saveTournament
import cz.ttcup.util.toLocalDate
import cz.ttcup.util.toLocalTime
import org.jsoup.Jsoup

suspend fun captureSchedule(capture: CaptureDto, restOfUrl: String) {
    val (dateStr, place) = "(\\d{2}\\.\\d{2}\\.\\d{4})/(\\w:\\d+)".toRegex().find(restOfUrl)!!.destructured
    val date = dateStr.toLocalDate("dd.MM.yyyy")
    logger.info("Captured a schedule page ${capture.url}")
    val doc = Jsoup.parse(capture.html)
    val table = doc.selectFirst("table.table tbody") ?: return;

    val matches = table.select("tr:not(.table-date)").map { row ->
        val time = row.child(1).text().trim().toLocalTime("HH:mm")

        val vsLink = row.child(2).selectFirst("a")!!.attr("href")
        val (player1Uid, player2Uid) = ".+/(.+)/vs/(.+)".toRegex().find(vsLink)!!.destructured

        val match = Match(time, listOf(player1Uid, player2Uid))

        val sets = row.child(3).text().trim();
        val setsFind = "(\\d+):(\\d+)".toRegex().find(sets)
        if (setsFind != null) {
            val (sets1, sets2) = setsFind.destructured;
            match.sets = listOf(sets1.toInt(), sets2.toInt())
        }

        val points = row.child(4).text().trim();
        val pointFind = "(\\d+):(\\d+)".toRegex().findAll(points).toList();
        if (pointFind.isNotEmpty()) {
            match.points = pointFind.map {
                val (points1, points2) = it.destructured;
                return@map "${points1.toInt()}:${points2.toInt()}"
            }
        }

        return@map match;
    }

    val players = getPlayers(matches.flatMap { it.playerUids })
    if (players.isEmpty()) return;

    val tournament = Tournament(date, place, matches);
    logger.info(
        "Saving tournament ${tournament.date}/${tournament.place} for players ${players.joinToString(", ") { it.uid }}"
    )

    saveTournament(tournament)
}
