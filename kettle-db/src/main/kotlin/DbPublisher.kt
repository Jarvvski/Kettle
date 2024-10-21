package com.github.jarvvski.bus.db

import com.github.jarvvski.bus.Broker
import com.github.jarvvski.bus.MessagePublisher
import com.github.jarvvski.bus.MessageSubscriber
import com.github.jarvvski.bus.Result4k
import com.github.kagkarlsson.scheduler.Scheduler
import com.github.kagkarlsson.scheduler.task.helper.Tasks
import javax.sql.DataSource

class DbBroker(
    dataSource: DataSource,
    subscribers: List<MessageSubscriber<*>>
): Broker {
    val scheduler = Scheduler.create(dataSource)
        .threads(2)
        .build()

    override fun start() {
        scheduler.start()
    }

    override fun stop() {
        scheduler.stop()
    }
}

//RecurringTask<Void> hourlyTask = Tasks.recurring("my-hourly-task", FixedDelay.ofHours(1))
//        .execute((inst, ctx) -> {
//            System.out.println("Executed!");
//        });

class DbPublisher(
    private val scheduler: Scheduler
) : MessagePublisher {
    override fun publish(message: Any): Result4k<Unit, Exception> {
        Tasks.oneTime("Some-Task", () -> )
    }

}
