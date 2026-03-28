package cz.ttcup.openapi.service

import cz.ttcup.util.ShortNamedEnum
import kotlin.enums.enumEntries

internal inline fun <reified T> createCustomEnum(
    schemas: MutableMap<String, MutableMap<String, Any>>,
    vararg replaceAt: List<String>
) where T : Enum<T>, T : ShortNamedEnum {
    schemas.put(T::class.simpleName!!, mutableMapOf<String, Any>().apply {
        replaceEnum(this, enumEntries<T>().map { it.shortName }, enumEntries<T>().map { it.name })
    })
    replaceAt.forEach {
        val schema = getSchema(schemas, it)
        schema.clear();
        schema.put("\$ref", "#/components/schemas/${T::class.simpleName!!}")
    }
}