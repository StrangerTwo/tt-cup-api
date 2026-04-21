package cz.ttcup.match.model

import cz.ttcup.player.model.Player
import org.bson.types.ObjectId

data class RivalMatches(
    val _id: ObjectId,

    val rival: Player? = null,

    val matches: List<Match>,
)