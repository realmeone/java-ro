package one.realme.krot.common.config

class BlockingWait {
    private val monitor = java.lang.Object()

    fun run() {
        synchronized(monitor) {
            monitor.wait()
        }
    }

    fun stop() {
        synchronized(monitor) {
            monitor.notifyAll()
        }
    }
}