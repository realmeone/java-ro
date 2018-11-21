package one.realme.krot.program

import com.google.common.base.Stopwatch
import com.google.common.util.concurrent.ServiceManager
import one.realme.krot.common.config.Configuration
import one.realme.krot.service.chain.ChainService
import one.realme.krot.service.discover.DiscoverService
import one.realme.krot.service.net.NetService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.management.ManagementFactory
import java.net.InetAddress
import java.util.concurrent.TimeUnit

object Krot {
    private val log: Logger = LoggerFactory.getLogger(Krot.javaClass)
    private val simpleName: String = javaClass.simpleName
    val config = Configuration()

    fun exec() {
        Banner.printBanner()
        val stopwatch: Stopwatch = Stopwatch.createStarted()
        log.info("Starting $simpleName on ${InetAddress.getLocalHost().hostName} with PID ${ManagementFactory.getRuntimeMXBean().name.split("@")[0]} by ${System.getProperty("user.name")}")

        // load config
        config.load("testnet.conf")

        // init services
        val chainService = ChainService()
        val netService = NetService(chainService)
        val discoverService = DiscoverService(chainService)

        // register services
        val serviceManager = ServiceManager(listOf(
                chainService, netService, discoverService
        ))

        // register shutdownhook
        Runtime.getRuntime().addShutdownHook(Thread {
            serviceManager.stopAsync().awaitStopped()
            log.info("ByeBye")
        })

        // start async
        serviceManager.startAsync()
        stopwatch.stop()
        log.info("Started $simpleName in ${stopwatch.elapsed(TimeUnit.SECONDS)} seconds")
    }
}
