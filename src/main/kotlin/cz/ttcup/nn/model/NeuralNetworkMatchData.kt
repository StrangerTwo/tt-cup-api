package cz.ttcup.nn.model

import kotlinx.serialization.Serializable

@Serializable
data class NeuralNetworkMatchData(
    val history: List<Byte>,
    val alreadyQualified: Byte,
    val isFinals: Byte,
    val result: Byte
)