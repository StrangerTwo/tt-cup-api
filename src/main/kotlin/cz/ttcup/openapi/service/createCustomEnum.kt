package cz.ttcup.openapi.service

import cz.ttcup.util.ShortNamedEnum
import kotlin.enums.enumEntries

internal inline fun <reified T> createCustomEnum(
    schemas: MutableMap<String, MutableMap<String, Any>>,
) where T : Enum<T>, T : ShortNamedEnum {
    val enumEntries = enumEntries<T>()
    val enumNames = enumEntries.map { it.name }
    schemas[T::class.simpleName!!] = mutableMapOf<String, Any>().apply {
        replaceEnum(
            this,
            enumEntries.map { it.shortName },
            enumNames
        )
    }
    // search for usages of enum and replace them with reference to enum
    schemas.values.forEach { schema ->
        val properties = schema["properties"] as MutableMap<String, MutableMap<String, Any>>?
        properties
            ?.values
            ?.filter { property ->
                property["type"] == "string"
                        && property["enum"] != null
                        && (property["enum"] as List<*>).let {
                    it.size == enumEntries.size && it.all { enum ->
                        enumNames.contains(enum)
                    }
                }
            }
            ?.forEach { property ->
                property.clear();
                property.put("\$ref", "#/components/schemas/${T::class.simpleName!!}")
            }
    }
}