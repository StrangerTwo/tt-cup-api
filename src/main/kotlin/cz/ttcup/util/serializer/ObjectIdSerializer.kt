package cz.ttcup.util.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bson.codecs.kotlinx.BsonDecoder
import org.bson.codecs.kotlinx.BsonEncoder
import org.bson.types.ObjectId

object ObjectIdSerializer : KSerializer<ObjectId> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ObjectId", PrimitiveKind.STRING)

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: ObjectId) {
        when (encoder) {
            is BsonEncoder -> encoder.encodeObjectId(value)
            else -> encoder.encodeString(value.toString())
        }
    }

    override fun deserialize(decoder: Decoder): ObjectId {
        return when (decoder) {
            is BsonDecoder -> decoder.decodeBsonValue().asObjectId().value
            else -> ObjectId(decoder.decodeString())
        }
    }

}
