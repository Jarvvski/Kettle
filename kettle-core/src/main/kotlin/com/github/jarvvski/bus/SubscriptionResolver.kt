package com.github.jarvvski.bus

import com.google.common.collect.ArrayListMultimap
import dev.forkhandles.result4k.resultFrom
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlin.reflect.KClass
import kotlin.reflect.full.allSupertypes

private val logger = KotlinLogging.logger {}

class SubscriptionResolver(
    subscribers: List<MessageSubscriber<*>>
) {
    private val storedSubscribers = ArrayListMultimap.create<String, MessageSubscriber<*>>()

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

    fun <T: Any> getSubscribers(
        kClass: KClass<T>
    ): Result4k<List<MessageSubscriber<T>>, Exception> {
        return resultFrom {
            storedSubscribers.get(kClass::class.simpleName !!).map {
                @Suppress("UNCHECKED_CAST") val typedSubscriber = it as? MessageSubscriber<T>
                logger.info {
                    "found subscriber[${it.javaClass.simpleName}] " +
                            "for payload[${kClass::class.simpleName}]"
                }
                // Yesssss, I knowwwww
                typedSubscriber !!
            }.toList()
        }
    }
}
