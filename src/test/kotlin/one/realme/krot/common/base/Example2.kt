package one.realme.krot.common.base

import kotlin.concurrent.thread

class Example2 {
    fun run() {
        Thread.sleep(5000)
        println("I'm working")
    }
}

fun main(args: Array<String>) {
    Runtime.getRuntime().addShutdownHook(Thread {
        println("shutdown now!")
    })

    thread {
        Example2().run()
    }

    println("started")
}