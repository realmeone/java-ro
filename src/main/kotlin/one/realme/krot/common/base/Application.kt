package one.realme.krot.common.base

import one.realme.krot.common.lang.measureTimeSeconds
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.management.ManagementFactory
import java.net.InetAddress

/**
 * Class that can be used to bootstrap and launch a application from a main
 * method. By default class will perform the following steps to bootstrap your
 * application.
 *
 * <code>
 * object MyApp {
 *
 *    // ... define service
 *
 *   fun run() {
 *     val app = Application(
 *         javaClass,
 *         listOf(serviceA, serviceB, serviceC)
 *     )
 *     app.run()
 *   }
 *
 *   @JvmStatic
 *   fun main(args : Array<String>){
 *       MyApp().run()
 *   }
 * }
 * </code>
 */
class Application(
        val name: String = "Application",
        private val confPath: String = "testnet.conf",
        servs: List<BaseService>,
        val logger: Logger = LoggerFactory.getLogger(Application::class.java)
) {
    // private fields
    private val startupShutdownMonitor = Any()
    private val shutdownHook: Thread by lazy {
        Thread {
            synchronized(startupShutdownMonitor) {
                doStop()
            }
        }
    }

    // access able fields
    val config: ApplicationConfig = ApplicationConfig()
    val services = mutableMapOf<String, BaseService>().also { map ->
        servs.forEach {
            map.putIfAbsent(it.name(), it)
        }
    }

    // lifecycle
    fun start() {
        synchronized(this.startupShutdownMonitor) {
            val timeElapsed = measureTimeSeconds {
                logEnvInfo()
                // 1.prepare configuration
                prepareConfiguration()
                // 2. init all services with application instance
                initServices()
                // 3. start services
                startServices()
                // 3. add shutdown hook
                registerShutdownHook()
            }
            logStartupInfo(timeElapsed)
        }
    }

    fun stop() {
        synchronized(this.startupShutdownMonitor) {
            doStop()
            unregisterShutdownHook()
        }
    }

    // start flow
    private fun logEnvInfo() {
        logger.info("Starting $name on ${InetAddress.getLocalHost().hostName} with PID ${ManagementFactory.getRuntimeMXBean().name.split("@")[0]} by ${System.getProperty("user.name")}")
    }

    private fun registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(this.shutdownHook)
    }

    private fun logStartupInfo(elapsed: Long) {
        logger.info("Started $name in $elapsed seconds")
    }

    private fun prepareConfiguration() {
        config.load(confPath)
    }

    // services
    private fun initServices() {
        services.forEach { _, it ->
            it.doInitialize(this)
        }
    }

    private fun startServices() {
        services.forEach { _, it ->
            it.doStart()
        }
    }


    // stop flow
    private fun doStop() {
        stopServices()
        logShutdownInfo()
    }

    private fun logShutdownInfo() {
        logger.info("ByeBye")
    }

    private fun unregisterShutdownHook() {
        try {
            Runtime.getRuntime().removeShutdownHook(shutdownHook) // avoid shutdown twice
        } catch (ignore: IllegalStateException) {
            // VM is already shutdown
        }
    }

    private fun stopServices() {
        services.forEach { _, it ->
            it.doStop()
        }
    }
}