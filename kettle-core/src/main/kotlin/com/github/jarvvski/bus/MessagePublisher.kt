package com.github.jarvvski.bus

interface MessagePublisher {
    fun publish(message: Any): Result4k<Unit, Exception>
}
