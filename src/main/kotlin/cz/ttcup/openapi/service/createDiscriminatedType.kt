package cz.ttcup.openapi.service

import cz.ttcup.match.model.CustomFilter
import cz.ttcup.util.ShortNamedEnum
import cz.ttcup.util.getByShortName
import kotlinx.serialization.SerialName
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

inline fun <reified T> createDiscriminatedType(
    schemas: MutableMap<String, MutableMap<String, Any>>, klass: KClass<CustomFilter>
) where T : Enum<T>, T : ShortNamedEnum {
    val subclasses = klass.sealedSubclasses;
    subclasses.forEach { subclass ->
        val serialName = subclass.findAnnotation<SerialName>()!!.value
        val enumType = getByShortName<T>(serialName)

        schemas[subclass.simpleName!!] = mutableMapOf<String, Any>().apply {
            put("type", "object")
            val properties = mutableMapOf(
                "type" to mapOf(klass.memberProperties.first().name to "string", "const" to enumType.shortName),
            )
            subclass.primaryConstructor
                ?.parameters
                ?.forEach { param ->
                    properties[param.name!!] = mapOf("type" to mapKotlinType(param.type))
                }

            put("properties", properties)
            put("required", properties.keys.toList())
        }
    }
    schemas[CustomFilter::class.simpleName]?.apply {
        remove("properties")
        put("oneOf", subclasses.map { subclass ->
            mapOf("\$ref" to "#/components/schemas/${subclass.simpleName!!}")
        })
    }
}