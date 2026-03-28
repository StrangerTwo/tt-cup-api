package cz.ttcup.system.service

fun isDev() = System.getenv("IS_DEV") == "true"
fun isLive() = !isDev()