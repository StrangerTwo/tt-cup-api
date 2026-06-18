package cz.ttcup.database.util

import com.mongodb.client.model.*
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

infix fun String.eq(value: Any): Bson {
    return Filters.eq(this, value);
}

infix fun <T> KProperty1<*, T>.ne(value: T): Bson {
    return Filters.ne(fieldName, value);
}

infix fun String.ne(value: Any): Bson {
    return Filters.ne(this, value);
}

infix fun <T> KProperty1<*, T>.set(value: T): Bson {
    if (value == null) return unset()
    return Updates.set(fieldName, value)
}

fun <T> KProperty1<*, T>.unset(): Bson {
    return Updates.unset(fieldName)
}

infix fun <T> KProperty1<*, T>.`in`(value: List<T>): Bson {
    return Filters.`in`(fieldName, value);
}

infix fun String.`in`(value: List<String>): Bson {
    return Filters.`in`(this, value);
}

infix fun <T> KProperty1<*, T>.contains(value: String): Bson {
    return this.regex(".*$value.*".toRegex(RegexOption.IGNORE_CASE))
}

infix fun <T> KProperty1<*, T>.regex(value: Regex): Bson {
    return Filters.regex(fieldName, value.toPattern())
}

infix fun String.contains(value: String): Bson {
    return this regex ".*$value.*".toRegex(RegexOption.IGNORE_CASE)
}

infix fun String.regex(value: Regex): Bson {
    return Filters.regex(this, value.toPattern())
}

infix fun <T> KProperty1<*, T>.elemMatch(value: Bson): Bson {
    return Filters.elemMatch(fieldName, value);
}

infix fun <T> KProperty1<*, T>.all(values: Iterable<Any>): Bson {
    return Filters.all(fieldName, values);
}

infix fun <T> KProperty1<*, T>.gte(value: Any): Bson {
    return Filters.gte(fieldName, value);
}

infix fun String.gte(value: Any): Bson {
    return Filters.gte(this, value);
}

infix fun <T> KProperty1<*, T>.lte(value: Any): Bson {
    return Filters.lte(fieldName, value);
}

infix fun String.lte(value: Any): Bson {
    return Filters.lte(this, value);
}

infix fun <T> KProperty1<*, T>.lt(value: Any): Bson {
    return Filters.lt(fieldName, value);
}

infix fun String.lt(value: Any): Bson {
    return Filters.lt(this, value);
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

fun <T> KProperty1<*, List<T?>?>.first(): Document {
    return Document("\$arrayElemAt", listOf("\$$fieldName", 0))
}

fun <T> KProperty1<*, List<T?>?>.last(): Document {
    return Document("\$arrayElemAt", listOf("\$$fieldName", -1))
}

fun <T> KProperty1<*, List<T?>?>.size(): Document {
    return Document("\$size", "\$$fieldName")
}

infix fun <T> KProperty1<*, T>.notRegex(value: Regex): Bson {
    return Document(fieldName, not(BsonRegularExpression(value.toPattern().pattern())))
}

fun <T> KProperty1<*, T>.exists(exists: Boolean = true): Bson {
    return Filters.exists(fieldName, exists)
}

fun <T> KProperty1<*, T>.asc(): Bson {
    return Sorts.ascending(fieldName)
}

fun <T> KProperty1<*, T>.desc(): Bson {
    return Sorts.descending(fieldName)
}

fun List<BsonElement>.toDocument(): BsonDocument = BsonDocument(this)

fun eq(valueA: Any, valueB: Any): Document {
    return Document(
        "\$eq",
        listOf(valueA, valueB),
    )
}

fun gt(valueA: Any, valueB: Any): Document {
    return Document(
        "\$gt",
        listOf(valueA, valueB),
    )
}

fun lt(valueA: Any, valueB: Any): Document {
    return Document(
        "\$lt",
        listOf(valueA, valueB),
    )
}

fun max(valueA: Any, valueB: Any): Document {
    return Document(
        "\$max",
        listOf(valueA, valueB),
    )
}

fun cond(condition: Any, ifTrue: Any, ifFalse: Any): Document {
    return Document(
        "\$cond",
        listOf(condition, ifTrue, ifFalse),
    )
}

fun not(value: Any): Document {
    return Document(
        "\$not",
        value
    )
}

fun abs(value: Any): Document {
    return Document(
        "\$abs",
        value
    )
}

fun subtract(a: String, b: String): Document {
    return Document(
        "\$subtract",
        listOf(a, b)
    )
}

fun getField(field: String, input: Any): Document {
    return Document(
        "\$getField",
        mapOf(
            "field" to field,
            "input" to input,
        )
    )
}

fun arrayElemAt(from: String, index: Int): Document {
    return Document(
        "\$arrayElemAt",
        listOf(from, index)
    )
}

fun lookup(
    from: String,
    `as`: String,
    localField: String? = null,
    foreignField: String? = null,
    pipeline: List<Bson>? = null,
    let: Map<String, Any>? = null,
): Document = Document(
    "\$lookup",
    Document(
        mutableMapOf<String, Any>(
            "from" to from,
            "as" to `as`,
        ).let {
            localField?.let { localField -> it.put("localField", localField) }
            foreignField?.let { foreignField -> it.put("foreignField", foreignField) }
            pipeline?.let { pipeline -> it.put("pipeline", pipeline) }
            let?.let { let -> it.put("let", let) }
            it
        }
    )
)

fun lookupFirst(
    from: String,
    `as`: String,
    localField: String? = null,
    foreignField: String? = null,
    pipeline: List<Bson>? = null,
    let: Map<String, Any>? = null,
) = listOf(
    lookup(
        from = from,
        `as` = `as`,
        localField = localField,
        foreignField = foreignField,
        pipeline = pipeline,
        let = let,
    ),
    Aggregates.addFields(
        Field(
            `as`,
            Document("\$first", "$${`as`}")
        )
    ),
).toTypedArray()