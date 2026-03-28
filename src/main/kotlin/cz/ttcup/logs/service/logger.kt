package cz.ttcup.logs.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory


inline val logger: Logger
    get() {
        val stackTrace = Throwable().stackTrace
        val caller = stackTrace.first { !it.className.startsWith("kotlin.") }
        return LoggerFactory.getLogger(caller.className.replace("\\$.*".toRegex(), ""))
    }