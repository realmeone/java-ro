package one.realme.krot.net.discover

import com.google.common.util.concurrent.AbstractExecutionThreadService
import org.slf4j.LoggerFactory

/**
 * Discover a peer, add peer to peers and start peer
 */
object DiscoverService : AbstractExecutionThreadService() {
    private val log = LoggerFactory.getLogger(DiscoverService.javaClass)
    private var port = 50506

    override fun startUp() {
    }

    override fun triggerShutdown() {
        log.info("DiscoverService stopped.")
    }

    override fun run() {
        log.info("DiscoverService started on port: $port")
    }
}