package one.realme.krot.common.base

import one.realme.krot.common.base.ServiceState.*

abstract class AbstractService : Lifecycle {
    private var state = Registered

    override fun isRunning() = Started == state

    fun doInitialize(app: Application) {
        if (Registered == state) {
            state = Initialized
            initialize(app)
        }
        assert(Initialized == state)
    }

    fun doStart() {
        if (Initialized == state) {
            state = Started
            start()
        }
        assert(Started == state)
    }

    fun doStop() {
        if (Started == state) {
            state = Stopped
            stop()
        }
    }

    fun state(): ServiceState = state
    open fun name(): String = this.javaClass.simpleName
    abstract fun initialize(app: Application)
}