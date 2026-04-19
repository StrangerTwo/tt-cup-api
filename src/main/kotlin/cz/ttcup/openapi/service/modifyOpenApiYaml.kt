package cz.ttcup.openapi.service

import cz.ttcup.player.model.Location

internal fun modifyOpenApiYaml(map: Map<*, *>) {
    // replace server url
    ((map["servers"] as List<*>)[0] as MutableMap<String, String>)["url"] = "https://api.ttcup.dev.mhdonline.cz"

    // ** Schemas **
    val paths = (map["paths"] as Map<*, Map<String, MutableMap<String, Any>>>)
    // ** Schemas **
    val schemas = (map["components"] as Map<*, *>)["schemas"] as MutableMap<String, MutableMap<String, Any>>

    // remove "Any" Object
    schemas.remove("Object")

    // mongo objectId
    schemas["ObjectId"] = mutableMapOf("type" to "string")

    (schemas["Match"]!!["properties"] as MutableMap<String, MutableMap<String, Any>>)["playerUids"]!!["prefixItems"] =
        listOf(
            mapOf("type" to "string"),
            mapOf("type" to "string")
        )
    (schemas["Match"]!!["properties"] as MutableMap<String, MutableMap<String, Any>>)["sets"]!!["prefixItems"] =
        listOf(
            mapOf("type" to "integer", "format" to "int32"),
            mapOf("type" to "integer", "format" to "int32")
        )

    createCustomEnum<Location>(
        schemas,
        listOf("GetPlayersRequest", "properties", "location"),
        listOf("Player", "properties", "location"),
        listOf("NewPlayer", "properties", "location"),
        listOf("EditPlayer", "properties", "location"),
    )

    schemas.entries.forEach { (schemaName, schema) ->
        val properties = schema["properties"] as MutableMap<String, MutableMap<String, *>>?
        properties?.let {
            // rename mongo objectId
            it["objectId"]?.let { objectId ->
                it["_id"] = objectId
                it.remove("objectId")
            }

            it.values.forEach { property ->
                property.remove("nullable")
            }
//            it.entries.removeIf { (key, value) -> (value as MutableMap<String, String>)["\$ref"] == "#/components/schemas/ObjectId" }
        }
        val required = schema["required"] as MutableList<String>?
        required?.apply {
            val index = indexOf("objectId")
            if (index != -1) set(index, "_id")
        }

        if (schemaName.startsWith("PaginatedQueryResult_")) {
            schema["required"] = listOf("results", "pageInfo")
        }
    }

    // use description as operationId
    paths.values.forEach { methodMap ->
        methodMap.values.forEach { operationMap ->
            val description = operationMap["description"] as String?
            if (description?.isNotBlank() == true) {
                operationMap["description"] = "";
                operationMap["operationId"] = description;
            }
        }
    }
}