package cz.ttcup.util.serializer

import cz.ttcup.util.ShortNamedEnum
import org.bson.codecs.Codec
import org.bson.codecs.configuration.CodecProvider
import org.bson.codecs.configuration.CodecRegistry

class ShortNamedEnumCodecProvider : CodecProvider {
    override fun <T> get(
        clazz: Class<T?>?,
        registry: CodecRegistry?
    ): Codec<T?>? {
        if (clazz?.isEnum != true) return null
        if (!ShortNamedEnum::class.java.isAssignableFrom(clazz)) return null

        val x: Class<ShortNamedEnum> = clazz as Class<ShortNamedEnum>
        return ShortNamedEnumCodec(x) as Codec<T?>?
    }

}