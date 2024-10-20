package com.github.jarvvski.bus.playground

import com.github.jarvvski.bus.LocalBroker
import com.github.jarvvski.bus.MessageSubscriber
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.resultFrom
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {

    val testSubscriber = TestMessageSubscriber()
    val localBroker = LocalBroker(listOf(testSubscriber))
    localBroker.start()
    val publisher = localBroker.publisher

    runBlocking(Dispatchers.Default) {
        publisher.publish(SampleMessage("Hello world!"))
        delay(7000)
    }

    localBroker.stop()
}


data class SampleMessage(
    val text: String
)

class TestMessageSubscriber : MessageSubscriber<SampleMessage> {
    override fun consume(message: SampleMessage): Result<Unit, Exception> {
        return resultFrom {
            logger.info { "Received message: ${message.text}" }
        }
    }
}
