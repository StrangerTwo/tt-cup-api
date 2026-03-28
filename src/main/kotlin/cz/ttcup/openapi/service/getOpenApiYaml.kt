package cz.ttcup.openapi.service

import io.ktor.server.routing.*
import org.yaml.snakeyaml.Yaml
import java.io.FileNotFoundException

fun RoutingContext.getOpenApiYaml(): String {
    val yamlContent = javaClass.classLoader.getResource("openapi/documentation.yaml")
        ?.readText()
        ?: throw FileNotFoundException("openapi/documentation.yaml")

    val yaml = Yaml()
    val map = yaml.load(yamlContent) as Map<*, *>

    modifyOpenApiYaml(map)

    return yaml.dump(map);
}

