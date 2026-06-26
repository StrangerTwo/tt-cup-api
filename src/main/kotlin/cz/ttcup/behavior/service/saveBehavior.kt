package cz.ttcup.behavior.service

import com.mongodb.client.model.UpdateOptions
import cz.ttcup.behavior.model.Behavior
import cz.ttcup.behavior.schema.behaviors
import cz.ttcup.context.service.withDatabase
import cz.ttcup.database.util.eq
import cz.ttcup.database.util.set

suspend fun saveBehavior(
    behavior: Behavior,
) {
    withDatabase {
        database.behaviors
            .updateOne(
                Behavior::playerUid eq behavior.playerUid,
                Behavior::behaviors set behavior.behaviors,
                UpdateOptions().upsert(true)
            )
    }
}