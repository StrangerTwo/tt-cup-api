package cz.ttcup.place.service

import com.mongodb.client.result.InsertOneResult
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.desc
import cz.ttcup.match.model.Match
import cz.ttcup.match.service.getMatch
import cz.ttcup.place.model.CreatePlaceByTournament
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.places
import cz.ttcup.tournament.service.getTournament
import io.ktor.server.plugins.*

suspend fun createPlaceFromTournament(request: CreatePlaceByTournament): InsertOneResult {
    val tournament = getTournament(request.tournamentId) ?: throw NotFoundException("Tournament not found")
    val latestMatch = getMatch(sort = Match::time.desc()) ?: throw NotFoundException("No matches found")

    withDatabase {
        val existingPlace = getPlace(tournament.place);
        if (existingPlace != null)
            throw IllegalArgumentException("Place ${existingPlace.uid} already exists")

        return database.places
            .insertOne(
                Place(
                    uid = tournament.place,
                    location = request.location,
                    lastUpdate = latestMatch.time
                )
            )
    }
}