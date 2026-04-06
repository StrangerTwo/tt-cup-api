package cz.ttcup.nn.service

import com.mongodb.client.model.Sorts
import cz.ttcup.context.service.withDatabase
import cz.ttcup.logs.service.logger
import cz.ttcup.nn.model.NeuralNetworkMatchData
import cz.ttcup.player.service.getAllPlayers
import cz.ttcup.tournament.schema.tournaments
import kotlinx.coroutines.flow.toList

suspend fun getNeuralNetworkMatchData(): List<NeuralNetworkMatchData> {
    val tournaments = withDatabase {
        database.tournaments
            .find()
            .sort(
                Sorts.orderBy(
                    Sorts.ascending("matches.time")
                )
            )
            .toList()
    }
    val allPlayers = getAllPlayers()

    val playerHistory: MutableMap<Pair<String, String>, ArrayDeque<Byte>> = mutableMapOf()
    val result = mutableListOf<NeuralNetworkMatchData>()

    tournaments.forEach { tournament ->
        tournament.matches.forEachIndexed { i, match ->
            if (match.sets == null) return@forEachIndexed

            // get related players
            val players = match.playerUids
                .mapNotNull { allPlayers.find { player -> player.uid == it } }

            // If it's not a saved player's match
            if (players.isEmpty()) return@forEachIndexed

            val historyId = match.playerUids.sorted().let { it[0] to it[1] }
            val history = playerHistory.computeIfAbsent(historyId) { ArrayDeque((1..5).map { -1 }) }

            val isWin = getMatchIsWin(match.sets, match.playerUids, historyId.first);
            val alreadyQualified = tournament.matches
                .slice(0 until i)
                .count { it.sets != null && getMatchIsWin(it.sets, it.playerUids, historyId.first) } >= 2;
            val isFinals = tournament.matches.size == 8 && i >= 6 // 7th or 8th matches are finals

            logger.info(
                "[${match.time} ${tournament.place} ${match.playerUids.joinToString("/")} ${
                    match.sets.joinToString(
                        ":"
                    )
                }]: $isWin $alreadyQualified $isFinals]"
            )

            // add the result
            result.add(
                NeuralNetworkMatchData(
                    history = history.toMutableList(),
                    alreadyQualified = alreadyQualified.toByte(),
                    isFinals = isFinals.toByte(),
                    result = isWin.toByte(),
                )
            )
            history.removeFirst()
            history.addLast(isWin.toByte())
        }
    }

    return result;
}

private fun getMatchIsWin(sets: List<Int>, playerUids: List<String>, forUid: String): Boolean {
    if (playerUids.none { playerUid -> playerUid == forUid }) return false
    val aWon = sets[0] > sets[1]
    val isA = forUid == playerUids[0]

    return if (isA) aWon else !aWon;
}

private fun Boolean.toByte(): Byte {
    return if (this) 1 else 0
}
