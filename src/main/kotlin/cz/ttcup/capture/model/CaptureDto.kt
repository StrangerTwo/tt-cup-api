package cz.ttcup.capture.model

import kotlinx.serialization.Serializable

@Serializable
data class CaptureDto(
    val url: String,
    val html: String
)