package com.github.jarvvski.bus.playground

import com.github.jarvvski.bus.Result4k
import java.time.ZoneId
import java.time.ZonedDateTime

interface TaskAdapter {
    fun submit(task: Task): Result4k<Unit, Exception>
    fun schedule(
        task: Task,
        zonedDateTime: ZonedDateTime
    ): Result4k<Unit, Exception>
}

interface DomainEventAdapter {
    fun emit(domainEvent: DomainEvent): Result4k<Unit, Exception>
    fun publish(
        topic: Topic,
        domainEvent: DomainEvent
    ): Result4k<Unit, Exception>
}

interface Task
interface DomainEvent
class Topic(val name: String)

data class CustomTaskPayload(
    val message: String,
    val number: Int
) : Task

data class CustomDomainEvent(
    val data: String
): DomainEvent

class MyService(
    val taskAdapter: TaskAdapter,
    val domainEventAdapter: DomainEventAdapter
){

    fun triggerSomeTask() {
        taskAdapter.submit(CustomTaskPayload("Hello World", 42))
    }

    fun triggerSomeTaskInNearFuture() {
        taskAdapter.schedule(
            CustomTaskPayload("Hello World", 42),
            ZonedDateTime.now(ZoneId.of("UTC")).plusMinutes(5)
        )
    }

    fun emitLocalDomainEvent() {
        domainEventAdapter.emit(CustomDomainEvent("Some Data"))
    }

    val myCustomTopic = Topic("my-custom-topic")
    fun emitRemoteDomainEvent() {
        domainEventAdapter.publish(
            myCustomTopic,
            CustomDomainEvent("Some Data")
        )
    }

}
