package one.realme.krot.service.net

import one.realme.krot.service.chain.ChainService

/**
 * hold active peers
 *
 * features
 *
 * 1. catch up to the head block
 * 2. broadcast tx or blk we received to the other peers
 * 3. send mined block to the other peers
 * 4. sort peers every 1 minutes
 *
 * read loop?
 * write loop?
 */
internal class SyncManager(
        chain: ChainService
) {
    private val peers = mutableMapOf<String, Peer>()
//    private val peerDb: PeerDb = PeerDb()

    internal fun getPeer(uniqueName: String): Peer? = peers[uniqueName]
    internal fun addPeer(uniqueName: String, peer: Peer) = peers.putIfAbsent(uniqueName, peer)
}