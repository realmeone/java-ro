package one.realme.krot.common.support

interface Lifecycle {
    fun start()
    fun stop()
    fun isRunning(): Boolean
}