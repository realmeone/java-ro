package one.realme.krot.service.discover

import one.realme.krot.common.appbase.AbstractService
import one.realme.krot.service.chain.ChainService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Discover a peer, add peer to peers and start peer
 */
class DiscoverService(val chainService: ChainService) : AbstractService() {
    private val log: Logger = LoggerFactory.getLogger(DiscoverService::class.java)
    private var port = 50506

    override fun name(): String = "DiscoverService"

    override fun initialize() {
    }

    override fun startup() {
        log.info("DiscoverService started on port: $port")
    }

    override fun shutdown() {
        log.info("DiscoverService stopped")
    }

}