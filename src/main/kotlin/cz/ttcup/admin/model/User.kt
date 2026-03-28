package cz.ttcup.admin.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String,
)