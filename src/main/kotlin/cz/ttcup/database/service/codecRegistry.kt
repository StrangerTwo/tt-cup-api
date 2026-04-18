package cz.ttcup.database.service

import com.mongodb.MongoClientSettings
import cz.ttcup.util.serializer.ShortNamedEnumCodecProvider
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry

internal fun codecRegistry(): CodecRegistry {
    val registry = CodecRegistries.fromRegistries(
        CodecRegistries.fromProviders(
            ShortNamedEnumCodecProvider()
        ),
        MongoClientSettings.getDefaultCodecRegistry()
    )
    return registry
}