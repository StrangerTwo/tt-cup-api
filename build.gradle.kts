val ktor_version: String by project
val yaml_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val mongo_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.11"
    kotlin("plugin.serialization") version "2.0.0"
}

group = "cz.ttcup"
version = "0.0.1"

application {
    mainClass.set("cz.ttcup.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.firebase:firebase-admin:9.4.3")
    implementation("com.google.maps:google-maps-services:2.2.0")

    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("org.jsoup:jsoup:1.14.3")

    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-host-common:${ktor_version}")
    implementation("io.ktor:ktor-server-status-pages:${ktor_version}")
    implementation("io.ktor:ktor-server-cors:${ktor_version}")
    implementation("io.ktor:ktor-server-call-logging:${ktor_version}")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-xml:$ktor_version")

    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")

    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:$mongo_version")
    implementation("org.mongodb:bson-kotlinx:$mongo_version")
    implementation("io.projectreactor:reactor-core:3.6.6")

    implementation("ch.qos.logback:logback-classic:${logback_version}")
    implementation("dev.kord:kord-core:0.15.0")

    implementation("org.yaml:snakeyaml:${yaml_version}")

    testImplementation("io.ktor:ktor-server-tests")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
