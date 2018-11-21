package one.realme.krot.program

import com.google.common.base.Stopwatch
import one.realme.krot.common.appbase.BlockingWait
import one.realme.krot.common.appbase.Configuration
import one.realme.krot.common.appbase.ServiceManager
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
    private val blockingWait = BlockingWait()
    private val shutdownHook = KrotShutdownHook()

    val config = Configuration()
    val services = ServiceManager()

    fun startup() {
        Banner.printBanner()

        val stopwatch: Stopwatch = Stopwatch.createStarted()
        log.info("Starting $simpleName on ${InetAddress.getLocalHost().hostName} with PID ${ManagementFactory.getRuntimeMXBean().name.split("@")[0]} by ${System.getProperty("user.name")}")

        // load config
        config.load("testnet.conf")

        // regist services
        val chainService = ChainService()
        services.registerService(
                chainService,
                NetService(chainService),
                DiscoverService(chainService)
        )

        // init services
        services.initialize()

        // register shutdownhook
        Runtime.getRuntime().addShutdownHook(shutdownHook)

        // start services
        services.startup()

        stopwatch.stop()
        log.info("Started $simpleName in ${stopwatch.elapsed(TimeUnit.SECONDS)} seconds")

        // wait all service done
        blockingWait.run()
    }


    fun shutdown() {
        // remove hook first
        Runtime.getRuntime().removeShutdownHook(shutdownHook)

        services.shutdown()
        log.info("ByeBye")
        blockingWait.stop()
    }

    internal class KrotShutdownHook : Thread() {
        override fun run() {
            try {
                Krot.shutdown()
            } catch (t: Throwable) {
                // ignore
            }
        }
    }
}
