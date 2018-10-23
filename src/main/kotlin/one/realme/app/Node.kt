package one.realme.app

import com.google.common.util.concurrent.ServiceManager
import one.realme.net.NetService
import org.slf4j.LoggerFactory
import java.lang.management.ManagementFactory
import java.net.InetAddress
import kotlin.system.measureTimeMillis

object Node {
    private val log = LoggerFactory.getLogger(Node.javaClass)

    fun launch() {
        val startElapsed = measureTimeMillis {
            log.info("Starting ${javaClass.simpleName} on ${InetAddress.getLocalHost().hostName} with PID ${ManagementFactory.getRuntimeMXBean().name.split("@")[0]} by ${System.getProperty("user.name")}")

            val services = mutableListOf(NetService)
            val serviceManager = ServiceManager(services)

            Runtime.getRuntime().addShutdownHook(Thread {
                log.info("Closing ${javaClass.simpleName} by ${System.getProperty("user.name")}")
                val stopElapsed = measureTimeMillis {
                    serviceManager.stopAsync().awaitStopped()
                }
                log.info("Closed ${javaClass.simpleName} in ${stopElapsed / 1000.0} seconds")
                log.info("ByeBye.")
            })

            serviceManager.startAsync()
        }
        log.info("Started ${javaClass.simpleName} in ${startElapsed / 1000.0} seconds")
    }
}