package one.realme.krot.program

import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.versionOption
import com.google.common.base.Stopwatch
import com.google.common.util.concurrent.ServiceManager
import one.realme.krot.common.Version
import one.realme.krot.common.lang.measureTimeSeconds
import one.realme.krot.module.net.discover.DiscoverService
import one.realme.krot.module.net.server.PeerService
import one.realme.krot.program.cmd.AddressCmd
import one.realme.krot.program.cmd.KrotCmd
import one.realme.krot.program.cmd.address.CreateCmd
import one.realme.krot.program.cmd.address.ListCmd
import org.slf4j.LoggerFactory
import java.lang.management.ManagementFactory
import java.net.InetAddress
import java.util.concurrent.TimeUnit

/**
 * A TCP Server
 */
object Krot {
    private val log = LoggerFactory.getLogger(Krot.javaClass)
    private val simpleName = javaClass.simpleName

    fun launch() {
        val stopwatch = Stopwatch.createStarted()
        log.info("Starting $simpleName on ${InetAddress.getLocalHost().hostName} with PID ${ManagementFactory.getRuntimeMXBean().name.split("@")[0]} by ${System.getProperty("user.name")}")

        // init config

        // init blockchain
        val context = AppContext()
        // init services, follow order
        val services = listOf(AppInitService(context), PeerService(context), DiscoverService())

        val serviceManager = ServiceManager(services)
        Runtime.getRuntime().addShutdownHook(Thread {
            log.info("Closing $simpleName by ${System.getProperty("user.name")}")
            val stopElapsed = measureTimeSeconds {
                serviceManager.stopAsync().awaitStopped()
            }
            log.info("Closed $simpleName in $stopElapsed seconds")
            log.info("ByeBye.")
        })

        serviceManager.startAsync()
        stopwatch.stop()
        log.info("Started $simpleName in ${stopwatch.elapsed(TimeUnit.SECONDS)} seconds")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        // parse args
        KrotCmd.versionOption(Version.CURRENT.toString(), message = { "Realme.One version $it" })
                .subcommands(
                        AddressCmd.subcommands(
                                CreateCmd,
                                ListCmd
                        )
                )
                .main(args)
    }
}