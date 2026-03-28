package cz.ttcup.openapi.service

import cz.ttcup.util.ShortNamedEnum
import kotlin.enums.enumEntries

internal inline fun <reified T> createEnumKeyedObject(
    schema: MutableMap<String, Any>,
    enumProps: () -> Map<String, Any>
) where T : Enum<T>, T : ShortNamedEnum {
    schema.clear();
    schema.put("type", "object");
    schema.put(
        "properties",
        enumEntries<T>().associate { it.shortName to enumProps() }
    );
}