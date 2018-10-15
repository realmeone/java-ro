package one.realme.app

import com.google.common.base.Stopwatch
import com.google.common.util.concurrent.ServiceManager
import one.realme.net.NetService
import org.slf4j.LoggerFactory
import java.lang.management.ManagementFactory
import java.net.InetAddress
import java.util.concurrent.TimeUnit

object App {
    private val log = LoggerFactory.getLogger(App.javaClass)

    @JvmStatic
    fun main(args: Array<String>) {
        Banner.printBanner()
        val stopwatch = Stopwatch.createStarted()
        log.info("Starting ${App.javaClass.simpleName} on ${InetAddress.getLocalHost().hostName} with PID ${ManagementFactory.getRuntimeMXBean().name.split("@")[0]} by ${System.getProperty("user.name")}")

        val services = mutableListOf(NetService)
        val serviceManager = ServiceManager(services)

        Runtime.getRuntime().addShutdownHook(Thread {
            stopwatch.reset()
            log.info("Closing ${App.javaClass.simpleName} by ${System.getProperty("user.name")}")
            stopwatch.start()
            serviceManager.stopAsync().awaitStopped()
            stopwatch.stop()
            log.info("Closed ${App.javaClass.simpleName} in ${stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")
            log.info("See you next time. ")
        })

        serviceManager.startAsync()

        stopwatch.stop()
        log.info("Started ${App.javaClass.simpleName} in ${stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")

    }
}