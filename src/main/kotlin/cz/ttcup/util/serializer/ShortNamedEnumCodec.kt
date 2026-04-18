package cz.ttcup.util.serializer

import cz.ttcup.util.ShortNamedEnum
import org.bson.BsonReader
import org.bson.BsonWriter
import org.bson.codecs.Codec
import org.bson.codecs.DecoderContext
import org.bson.codecs.EncoderContext

class ShortNamedEnumCodec(
    private val enumClass: Class<ShortNamedEnum>
) : Codec<ShortNamedEnum> {

    override fun encode(
        writer: BsonWriter,
        value: ShortNamedEnum,
        encoderContext: EncoderContext
    ) {
        writer.writeString(value.shortName)
    }

    override fun decode(
        reader: BsonReader,
        decoderContext: DecoderContext
    ): ShortNamedEnum {
        val shortName = reader.readString()

        return enumClass.enumConstants
            .firstOrNull { it.shortName == shortName }
            ?: throw IllegalArgumentException(
                "Unknown shortName '$shortName' for enum ${enumClass.simpleName}"
            )
    }

    override fun getEncoderClass(): Class<ShortNamedEnum> = enumClass
}
