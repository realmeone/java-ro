package one.realme.krot.common.base

interface Lifecycle {
    fun start()
    fun stop()
    fun isRunning(): Boolean
}