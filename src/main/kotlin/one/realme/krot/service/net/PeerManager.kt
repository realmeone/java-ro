package one.realme.krot.service.net

/**
 * peer manager hold peers
 */
class PeerManager {
    private val peers = mutableMapOf<String, Peer>()
//    private val peerDb: PeerDb = PeerDb()

    internal fun getPeer(uniqueName: String): Peer? = peers[uniqueName]
    internal fun addPeer(uniqueName: String, peer: Peer) = peers.putIfAbsent(uniqueName, peer)
}