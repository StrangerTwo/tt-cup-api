package cz.ttcup.util.module

import cz.ttcup.logs.service.logger
import io.ktor.server.application.*
import kotlinx.coroutines.*
import java.time.Duration
import kotlin.time.toKotlinDuration

@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
fun Application.launchRepeating(
    name: String,
    delayFn: () -> Duration,
    body: suspend Application.() -> Unit
) {
    launch(newSingleThreadContext(name)) {
        while (isActive) {
            try {
                val delay = delayFn()

                logger.info("$name starting in: $delay")
                delay(delay.toKotlinDuration())

                body()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Throwable) {
                logger.error("$name completed with error", e)
            }
        }
    }
}