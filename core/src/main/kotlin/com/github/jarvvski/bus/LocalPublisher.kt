package com.github.jarvvski.bus

import com.google.common.collect.ArrayListMultimap
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
) {
    // really rough impl, need better datastore
    private val messages: Queue<Pair<UUID, Any>> = LinkedList()
    private val storedSubscribers = ArrayListMultimap.create<String, MessageSubscriber<*>>()
    val publisher = LocalPublisher(messages)

    private val supervisor = SupervisorJob()
    private val scope = CoroutineScope(supervisor)

    private var job: Job? = null

    init {
        // resolve subscribers to map of eventType to subscriber
        // so that we can easily route events to the given subscribers in the
        // #run function
        subscribers.forEach {
            logger.info {
                "Resolving subscriber[${it::class.simpleName}]"
            }

            // yes, I know !! is bad :)
            val type = it::class.allSupertypes.first {
                it.classifier == MessageSubscriber::class
            }.arguments[0].type !!
            val simpleName = type.classifier?.let { (it as? KClass<*>)?.simpleName }

            logger.info {
                "Found message type[${type}] on subscriber[${it::class.simpleName}]"
            }

            storedSubscribers.put(simpleName, it)
        }

    }

    fun start() {
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
            val payload = message.second
            storedSubscribers.get(payload::class.simpleName !!).forEach {
                @Suppress("UNCHECKED_CAST") val typedSubscriber = it as? MessageSubscriber<Any>
                logger.info {
                    "invoking subscriber[${it.javaClass.simpleName}] for payload[${payload::class.simpleName}]"
                }
                // Yesssss, I knowwwww
                typedSubscriber !!.consume(payload)
            }
            logger.info {
                "removing message from queue"
            }
        }
    }

    fun stop() {
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
