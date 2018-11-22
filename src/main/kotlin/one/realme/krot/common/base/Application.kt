package one.realme.krot.common.base

import one.realme.krot.common.config.Configuration
import one.realme.krot.common.lang.measureTimeSeconds
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Class that can be used to bootstrap and launch a application from a main
 * method. By default class will perform the following steps to bootstrap your
 * application.
 *
 * <code>
 * class Krot : CliktCommand(name = "krot", invokeWithoutSubcommand = true) {
 *
 *    // ... define service
 *
 *   fun run() {
 *     val app = Application(
 *         listOf(
 *             serviceA, serviceB, serviceC
 *         ))
 *     app.run();
 *   }
 *
 *   @JvmStatic
 *   fun main(args : Array<String>){
 *       Krot().main(args)
 *   }
 * }
 * </code>
 */
class Application(vararg servs: AbstractService) {
    private val log: Logger = LoggerFactory.getLogger(Application::class.java)
    private val defaultConfPath = "testnet.conf"
    private val startupShutdownMonitor = Any()
    private var shutdownHook: Thread? = null

    // service can get config and other services
    val config: Configuration = Configuration()
    val services = mutableMapOf<String, AbstractService>()

    init {
        servs.forEach {
            services.putIfAbsent(it.name(), it)
        }
    }

    // lifecycle
    fun start() {
        synchronized(this.startupShutdownMonitor) {
            val timeElapsed = measureTimeSeconds {
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
            if (this.shutdownHook != null) {
                try {
                    Runtime.getRuntime().removeShutdownHook(shutdownHook) // avoid shutdown twice
                } catch (ignore: IllegalStateException) {
                    // VM is already shutdown
                }
            }
        }
    }

    // private methods
    private fun doStop() {
        stopServices()
    }

    private fun registerShutdownHook() {
        if (this.shutdownHook == null) {
            this.shutdownHook = Thread {
                synchronized(startupShutdownMonitor) {
                    doStop()
                }
            }
            Runtime.getRuntime().addShutdownHook(this.shutdownHook)
        }
    }

    private fun logStartupInfo(elapsed: Long) {
        log.info("Started ${Application::class.java.simpleName} in $elapsed seconds")
    }

    private fun prepareConfiguration() {
        config.load(defaultConfPath)
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

    private fun stopServices() {
        services.forEach { _, it ->
            it.doStop()
        }
    }
}