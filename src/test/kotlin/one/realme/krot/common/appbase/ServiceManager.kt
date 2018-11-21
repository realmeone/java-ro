package one.realme.krot.common.appbase

class ServiceManager {
    private val services = mutableMapOf<String, AbstractService>()
    private val initializedServices = mutableListOf<AbstractService>()
    private val runningServices = mutableListOf<AbstractService>()

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

    fun startup() {
        try {
            initializedServices.forEach { service ->
                service.doStartup()
                runningServices.add(service)
            }
        } catch (e: Exception) {
            shutdown()
            throw e
        }
    }

    fun shutdown() {
        try {
            runningServices.forEach { service ->
                service.doShutdown()
            }
        } finally {
            initializedServices.clear()
            runningServices.clear()
        }
    }
}