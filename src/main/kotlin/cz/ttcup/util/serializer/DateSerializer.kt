package cz.ttcup.util.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bson.BsonDateTime
import org.bson.codecs.kotlinx.BsonDecoder
import org.bson.codecs.kotlinx.BsonEncoder
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object LocalDateSerializer : KSerializer<LocalDate> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        when (encoder) {
            is BsonEncoder -> encoder.encodeBsonValue(
                BsonDateTime(
                    value.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
                )
            )

            else -> encoder.encodeString(value.format(DateTimeFormatter.ISO_DATE))
        }
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return when (decoder) {
            is BsonDecoder -> Instant.ofEpochMilli(decoder.decodeBsonValue().asDateTime().value).atZone(ZoneOffset.UTC)
                .toLocalDate()

            else -> LocalDate.parse(decoder.decodeString())
        }
    }
}

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        when (encoder) {
            is BsonEncoder -> encoder.encodeBsonValue(BsonDateTime(value.toInstant(ZoneOffset.UTC).toEpochMilli()))
            else -> encoder.encodeString(value.format(DateTimeFormatter.ISO_DATE_TIME))
        }
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return when (decoder) {
            is BsonDecoder -> Instant.ofEpochMilli(decoder.decodeBsonValue().asDateTime().value).atZone(ZoneOffset.UTC)
                .toLocalDateTime()

            else -> decoder.decodeString().let {
                try {
                    LocalDateTime.parse(it)
                } catch (e: DateTimeParseException) {
                    ZonedDateTime.parse(it).toLocalDateTime()
                }
            }
        }
    }
}

object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalTime) {
        when (encoder) {
            is BsonEncoder -> encoder.encodeBsonValue(
                BsonDateTime(
                    value.atDate(LocalDate.of(1970, 1, 1)).toInstant(ZoneOffset.UTC).toEpochMilli()
                )
            )

            else -> encoder.encodeString(value.format(DateTimeFormatter.ISO_TIME))
        }
    }

    override fun deserialize(decoder: Decoder): LocalTime {
        return when (decoder) {
            is BsonDecoder -> Instant.ofEpochMilli(decoder.decodeBsonValue().asDateTime().value).atZone(ZoneOffset.UTC)
                .toLocalTime()

            else -> LocalTime.parse(decoder.decodeString())
        }
    }
}