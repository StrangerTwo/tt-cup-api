package cz.ttcup.behavior.model

import cz.ttcup.util.serializer.ObjectIdSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Behavior(
    val playerUid: String,
    val behaviors: List<PlayerBehavior>,

    @SerialName("_id")
    @Serializable(with = ObjectIdSerializer::class)
    val objectId: ObjectId
)