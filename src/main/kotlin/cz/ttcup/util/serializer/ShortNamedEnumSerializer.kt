package cz.ttcup.util.serializer

import cz.ttcup.util.ShortNamedEnum
import cz.ttcup.util.getByShortName
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

inline fun <reified T> shortNameEnumSerializer(): KSerializer<T>
        where T : Enum<T>, T : ShortNamedEnum = object : KSerializer<T> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(T::class.simpleName!!, PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(value.shortName)
    }

    override fun deserialize(decoder: Decoder): T {
        val shortName = decoder.decodeString()
        return getByShortName(shortName)
    }
}
