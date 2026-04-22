package cz.ttcup.place.model

import kotlinx.serialization.Serializable

@Serializable
class OutdatedPlace(
    val place: Place,
    val updateUrl: String,
)