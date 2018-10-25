package one.realme.krot.app

import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.versionOption
import com.google.common.util.concurrent.ServiceManager
import one.realme.krot.app.cmd.AddressCmd
import one.realme.krot.app.cmd.KrotCmd
import one.realme.krot.app.cmd.address.CreateCmd
import one.realme.krot.app.cmd.address.ListCmd
import one.realme.krot.net.client.DiscoverService
import one.realme.krot.net.server.NetService
import org.slf4j.LoggerFactory
import java.lang.management.ManagementFactory
import java.net.InetAddress
import kotlin.system.measureTimeMillis

/**
 * Name like go-ethereum -> geth
 */
object Krot {
    private val log = LoggerFactory.getLogger(Krot.javaClass)
    private val simpleName = javaClass.simpleName

    fun launch() {
        val startElapsed = measureTimeMillis {
            log.info("Starting $simpleName on ${InetAddress.getLocalHost().hostName} with PID ${ManagementFactory.getRuntimeMXBean().name.split("@")[0]} by ${System.getProperty("user.name")}")

            val services = mutableListOf(
                    NetService,
                    DiscoverService
            )

            val serviceManager = ServiceManager(services)

            Runtime.getRuntime().addShutdownHook(Thread {
                log.info("Closing $simpleName by ${System.getProperty("user.name")}")
                val stopElapsed = measureTimeMillis {
                    serviceManager.stopAsync().awaitStopped()
                }
                log.info("Closed $simpleName in ${stopElapsed / 1000.0} seconds")
                log.info("ByeBye.")
            })

            serviceManager.startAsync()
        }
        log.info("Started $simpleName in ${startElapsed / 1000.0} seconds")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        // parse args
        KrotCmd.versionOption("1", message = { "Realme.One version $it" })
                .subcommands(
                        AddressCmd.subcommands(
                                CreateCmd,
                                ListCmd
                        )
                )
                .main(args)
    }
}