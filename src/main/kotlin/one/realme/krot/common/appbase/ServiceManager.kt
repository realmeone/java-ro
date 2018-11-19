package one.realme.krot.common.appbase

class ServiceManager {
    private val services = mutableMapOf<String, AbstractService>()
    private val initializedServices = mutableListOf<AbstractService>()
    private val runningServices = mutableListOf<AbstractService>()

    fun registerService(service: AbstractService) {
        services.putIfAbsent(service.name(), service)
    }

    fun initialize(config: Configuration) {
        services.forEach { _, service ->
            service.initialize(config)
            initializedServices.add(service)
        }
    }

    fun startup() {
        try {
            initializedServices.forEach { service ->
                service.startup()
                runningServices.add(service)
            }
        } catch (e: Exception) {
            shutdown()
            throw e
        }
    }

    fun shutdown() {
        try {
            runningServices.forEach {
                it.shutdown()
            }
        } finally {
            initializedServices.clear()
            runningServices.clear()
        }
    }
}