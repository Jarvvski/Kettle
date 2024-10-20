package com.github.jarvvski.bus

interface MessageSubscriber<T> {
    fun consume(message: T): Result4k<Unit, Exception>
}
