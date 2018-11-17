package one.realme.krot.program

import one.realme.krot.service.chain.ChainService
import one.realme.krot.service.discover.DiscoverService
import one.realme.krot.service.net.server.PeerService

/**
 * Not thread safe
 */
object Context {
    lateinit var config: Configuration
    lateinit var chainService: ChainService
    lateinit var peerService: PeerService
    lateinit var discoverService: DiscoverService
}