package cz.ttcup.openapi.service

internal fun replaceEnum(enumShema: MutableMap<String, Any>, values: List<String>, names: List<String>) {
    enumShema.put("type", "string")
    enumShema.put("enum", values)
    enumShema.put("x-enumNames", names)
}