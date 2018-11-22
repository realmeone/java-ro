package one.realme.krot.common.support


class ServiceManager : Lifecycle {
    private val services = mutableMapOf<String, AbstractService>()
    private val initializedServices = mutableListOf<AbstractService>()
    private val runningServices = mutableListOf<AbstractService>()

    @Volatile
    private var running: Boolean = false

    fun registerService(vararg servs: AbstractService) {
        servs.forEach { service ->
            services.getOrPut(service.name()) { service }
        }
    }

    fun findService(name: String): AbstractService? = services[name]

    fun initialize() {
        services.forEach { _, service ->
            service.doInitialize()
            initializedServices.add(service)
        }
    }

    override fun isRunning(): Boolean = true

    override fun start() {
        try {
            initializedServices.forEach { service ->
                service.doStart()
                runningServices.add(service)
            }
            this.running = true
        } catch (e: Exception) {
            stop()
            throw e
        }
    }

    override fun stop() {
        try {
            runningServices.forEach { service ->
                service.doStop()
            }
            this.running = false
        } finally {
            initializedServices.clear()
            runningServices.clear()
        }
    }
}