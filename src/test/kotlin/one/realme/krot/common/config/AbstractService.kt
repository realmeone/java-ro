package one.realme.krot.common.config

import one.realme.krot.common.config.ServiceState.*

abstract class AbstractService {
    private var state = Registered

    fun isRunning() = Started == state

    fun doInitialize() {
        if (Registered == state) {
            state = Initialized
            initialize()
        }
        assert(Initialized == state)
    }

    fun doStartup() {
        if (Initialized == state) {
            state = Started
            startup()
        }
        assert(Started == state)
    }

    fun doShutdown() {
        if (Started == state) {
            state = Stopped
            shutdown()
        }
    }

    fun state(): ServiceState = state
    abstract fun name(): String
    abstract fun initialize()
    abstract fun startup()
    abstract fun shutdown()
    fun isAsync(): Boolean = false
}