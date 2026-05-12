package cz.ttcup.openapi.service

import kotlin.reflect.KType

fun mapKotlinType(type: KType): String {
    return when (type.classifier) {
        Int::class -> "integer"
        String::class -> "string"
        Boolean::class -> "boolean"
        Double::class -> "number"
        else -> "object"
    }
}