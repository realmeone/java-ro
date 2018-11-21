package one.realme.krot.service.discover

import com.google.common.util.concurrent.AbstractExecutionThreadService
import one.realme.krot.service.chain.ChainService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Discover a peer, add peer to peers and start peer
 */
class DiscoverService(val chainService: ChainService) : AbstractExecutionThreadService() {
    private val log: Logger = LoggerFactory.getLogger(DiscoverService::class.java)
    private var port = 50506

    override fun run() {
        log.info("DiscoverService started on port: $port")
    }

    override fun triggerShutdown() {
        log.info("DiscoverService stopped")
    }

}