package cz.ttcup.place.service

import com.mongodb.client.model.Filters
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.contains
import cz.ttcup.database.util.eq
import cz.ttcup.place.model.GetPlacesRequest
import cz.ttcup.place.model.Place
import cz.ttcup.place.schema.places
import cz.ttcup.util.model.PaginatedQueryResult
import cz.ttcup.util.service.queryPaginated
import kotlinx.coroutines.flow.toList
import org.bson.BsonDocument
import org.bson.conversions.Bson

suspend fun getPlaces(request: GetPlacesRequest): PaginatedQueryResult<Place> {
    withDatabase {
        return database.places
            .queryPaginated(
                request.pageInfo,
                listOfNotNull(
                    request.search?.let { Place::name contains it },
                    request.location?.let { Place::location eq it },
                ).takeIf {
                    it.isNotEmpty()
                }?.let {
                    Filters.and(it)
                }
            )
    }
}

suspend fun getPlaces(filter: Bson?): List<Place> = withDatabase {
    return database.places
        .find(filter ?: BsonDocument())
        .toList()
}