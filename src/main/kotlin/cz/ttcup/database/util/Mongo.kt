package cz.ttcup.database.util

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.serialization.SerialName
import org.bson.*
import org.bson.conversions.Bson
import kotlin.reflect.KProperty1

val <T> KProperty1<*, T>.fieldName: String
    get() = this.annotations
        .find { it is SerialName }
        ?.let { (it as SerialName).value }
        ?: this.name;

infix fun <T> KProperty1<*, T>.eq(value: T): Bson {
    return Filters.eq(fieldName, value);
}

infix fun <T> KProperty1<*, T>.set(value: T): Bson {
    return Updates.set(fieldName, value)
}

infix fun <T> KProperty1<*, T>.`in`(value: List<T>): Bson {
    return Filters.`in`(fieldName, value);
}

infix fun <T> KProperty1<*, T>.elemMatch(value: Bson): Bson {
    return Filters.elemMatch(fieldName, value);
}

infix fun <T> KProperty1<*, T>.gte(value: Any): Bson {
    return Filters.gte(fieldName, value);
}

infix fun <T> KProperty1<*, T>.lte(value: Any): Bson {
    return Filters.lte(fieldName, value);
}

infix fun <T> KProperty1<*, T>.el(value: Int): BsonElement {
    return BsonElement(fieldName, BsonInt32(value))
}

infix fun <T> KProperty1<*, T>.el(value: String): BsonElement {
    return BsonElement(fieldName, BsonString(value))
}

infix fun <T> KProperty1<*, T>.el(value: Long): BsonElement {
    return BsonElement(fieldName, BsonInt64(value))
}

infix fun <T> KProperty1<*, T>.el(value: BsonDocument): BsonElement {
    return BsonElement(fieldName, value)
}

fun List<BsonElement>.toDocument(): BsonDocument = BsonDocument(this)