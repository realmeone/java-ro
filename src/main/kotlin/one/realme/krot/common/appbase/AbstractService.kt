package one.realme.krot.common.appbase

import one.realme.krot.common.appbase.ServiceState.*

abstract class AbstractService : Service {
    private var state = Registered

    override fun state(): ServiceState = state

    override fun initialize(config: Configuration) {
        if (Registered == state) {
            doInitialize(config)
            state = Initialized
        }
        assert(Initialized == state)
    }

    override fun startup() {
        if (Initialized == state) {
            doStartup()
            state = Started
        }
        assert(Started == state)
    }

    override fun shutdown() {
        if (Started == state) {
            doShutdown()
            state = Stopped
        }
        assert(Stopped == state)
    }

    abstract fun doInitialize(config: Configuration)
    abstract fun doStartup()
    abstract fun doShutdown()
}