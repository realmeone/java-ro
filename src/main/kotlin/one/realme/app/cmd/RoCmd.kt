package one.realme.app.cmd

import com.github.ajalt.clikt.core.CliktCommand
import com.google.common.util.concurrent.ServiceManager
import one.realme.app.App
import one.realme.app.Banner
import one.realme.net.NetService
import java.lang.management.ManagementFactory
import java.net.InetAddress
import kotlin.system.measureTimeMillis

/**
 * ro the main entry point into the system if no special subcommand is ran.
 */
class RoCmd : CliktCommand(name = "ro") {
    override fun run() {
        Banner.printBanner()
        val startElapsed = measureTimeMillis {
            App.log.info("Starting ${javaClass.simpleName} on ${InetAddress.getLocalHost().hostName} with PID ${ManagementFactory.getRuntimeMXBean().name.split("@")[0]} by ${System.getProperty("user.name")}")

            val services = mutableListOf(NetService)
            val serviceManager = ServiceManager(services)

            Runtime.getRuntime().addShutdownHook(Thread {
                App.log.info("Closing ${javaClass.simpleName} by ${System.getProperty("user.name")}")
                val stopElapsed = measureTimeMillis {
                    serviceManager.stopAsync().awaitStopped()
                }
                App.log.info("Closed ${javaClass.simpleName} in ${stopElapsed / 1000.0} seconds")
                App.log.info("ByeBye.")
            })

            serviceManager.startAsync()
        }
        App.log.info("Started ${App.javaClass.simpleName} in ${startElapsed / 1000.0} seconds")
    }

}