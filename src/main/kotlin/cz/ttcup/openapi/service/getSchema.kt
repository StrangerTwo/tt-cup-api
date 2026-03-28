package cz.ttcup.openapi.service

internal fun getSchema(
    schema: Map<*, *>,
    keys: List<String>
): MutableMap<String, Any> {
    var currentSchema: MutableMap<String, *> = schema as MutableMap<String, *>;
    for (key in keys) {
        currentSchema =
            (currentSchema[key] ?: throw NullPointerException("key $key is not in schema")) as MutableMap<String, Any>
    }
    return currentSchema as MutableMap<String, Any>;
}