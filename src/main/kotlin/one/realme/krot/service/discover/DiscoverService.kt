package one.realme.krot.service.discover

import one.realme.krot.common.base.AbstractService
import one.realme.krot.common.base.Application
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Discover a peer, add peer to peers and start peer
 */
class DiscoverService : AbstractService() {
    private val log: Logger = LoggerFactory.getLogger(DiscoverService::class.java)
    private var port = 50506

    override fun initialize(app: Application) {

    }

    override fun start() {
        log.info("DiscoverService started on port: $port")
    }

    override fun stop() {
        log.info("DiscoverService stopped")
    }

}