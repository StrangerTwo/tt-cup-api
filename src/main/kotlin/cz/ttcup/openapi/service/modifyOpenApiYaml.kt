package cz.ttcup.openapi.service

internal fun modifyOpenApiYaml(map: Map<*, *>) {
    // replace server url
    ((map["servers"] as List<*>)[0] as MutableMap<String, String>).put("url", "https://api.ttcup.dev.mhdonline.cz")

    // ** Schemas **
    val paths = (map["paths"] as Map<*, Map<String, MutableMap<String, Any>>>)
    // ** Schemas **
    val schemas = (map["components"] as Map<*, *>)["schemas"] as MutableMap<String, MutableMap<String, Any>>

    // remove "Any" Object
    schemas.remove("Object")

    // mongo objectId
    schemas["ObjectId"] = mutableMapOf("type" to "string")

//    schemas.values.forEach { schema ->
//        val properties = schema["properties"] as MutableMap<String, *>?
//        properties?.let {
//            // remove mongo objectId
//            it.remove("objectId")
//            it.entries.removeIf { (key, value) -> (value as MutableMap<String, String>)["\$ref"] == "#/components/schemas/ObjectId" }
//        }
//    }

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