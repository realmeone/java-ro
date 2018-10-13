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
        log.info("Starting ${App.javaClass.simpleName} on ${InetAddress.getLocalHost().hostName} with PID ${ManagementFactory.getRuntimeMXBean().name.split("@")[0]} started by ${System.getProperty("user.name")}")

        val services = mutableListOf(NetService)
        val serviceManager = ServiceManager(services)
        serviceManager.startAsync()

        stopwatch.stop()
        log.info("Started ${App.javaClass.simpleName} in ${stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")

        Runtime.getRuntime().addShutdownHook(Thread {
            println("Realme one is closing...")
            serviceManager.stopAsync()
            println("Realme one is closed.")
            println("See you next time. ")
        })
    }
}