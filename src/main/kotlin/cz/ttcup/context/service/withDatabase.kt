package cz.ttcup.context.service

import cz.ttcup.context.model.DatabaseContext
import cz.ttcup.database.service.withDatabase

inline fun <T : Any> withDatabase(body: DatabaseContext.() -> T): T {
    return withDatabase("tt-cup", body);
}