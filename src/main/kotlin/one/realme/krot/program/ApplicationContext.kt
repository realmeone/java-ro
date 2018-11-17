package one.realme.krot.program

import one.realme.krot.module.chain.ChainService
import one.realme.krot.module.discover.DiscoverService
import one.realme.krot.module.net.server.PeerService

/**
 * Not thread safe
 */
class ApplicationContext {
    lateinit var config: ApplicationConfig
    lateinit var chainService: ChainService
    lateinit var peerService: PeerService
    lateinit var discoverService: DiscoverService
}