package cz.ttcup.nn.model

import kotlinx.serialization.Serializable

@Serializable
data class NeuralNetworkMatchData(
    val history: List<Int>,
    val alreadyQualified: Int,
    val isFinals: Int,
    val lastMatchesWon: Double,
    val result: Int
)