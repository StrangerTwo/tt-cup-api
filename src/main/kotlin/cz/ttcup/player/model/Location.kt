package cz.ttcup.player.model

import cz.ttcup.util.ShortNamedEnum
import cz.ttcup.util.serializer.shortNameEnumSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

@Serializable(with = Location.Companion.Serializer::class)
enum class Location(
    override val shortName: String,
) : ShortNamedEnum {
    HAVLICKUV_BROD("B"),
    PRAHA("P"),
    HAVIROV("H")
    ;

    companion object {
        object Serializer : KSerializer<Location> by shortNameEnumSerializer()
    }
}