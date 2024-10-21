package com.github.jarvvski.bus

import com.google.common.collect.ArrayListMultimap
import dev.forkhandles.result4k.flatMap
import dev.forkhandles.result4k.map
import dev.forkhandles.result4k.orThrow
import dev.forkhandles.result4k.resultFrom
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.LinkedList
import java.util.Queue
import java.util.UUID
import java.util.concurrent.Flow.Subscriber
import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes

private val logger = KotlinLogging.logger {}

class LocalBroker(
    subscribers: List<MessageSubscriber<*>>
) : Broker {
    // really rough impl, need better datastore
    private val messages: Queue<Pair<UUID, Any>> = LinkedList()
    val publisher = LocalPublisher(messages)

    private val supervisor = SupervisorJob()
    private val resolver = SubscriptionResolver(subscribers)
    private val scope = CoroutineScope(supervisor)

    private var job: Job? = null

    override fun start() {
        job = scope.launch {
            while (true) {
                work()
                delay(2500)
            }
        }
    }

    private fun work() {
        // need a better way to poll map routinely for changes
        while (messages.isNotEmpty()) {
            val message = messages.poll()
            val payload: Any = message.second
            resolver.getSubscribers(payload::class).orThrow()
                .forEach {
                    logger.info {
                        "invoking subscriber[${it.javaClass.simpleName}] " +
                                "for payload[${payload::class.simpleName}]"
                    }
                    it.consume(payload)
                }
        }

        logger.info {
            "removing message from queue"
        }
    }


    override fun stop() {
        job?.cancel()
    }
}

class LocalPublisher(
    private val queue: Queue<Pair<UUID, Any>>
) : MessagePublisher {
    override fun publish(message: Any): Result4k<Unit, Exception> {
        return resultFrom {
            val key = UUID.randomUUID()
            queue.add(key to message)
        }
    }
}
