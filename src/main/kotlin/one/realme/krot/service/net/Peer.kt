package one.realme.krot.service.net

import io.netty.channel.Channel
import one.realme.krot.net.Protocol
import one.realme.krot.service.chain.ChainService

class Peer(
        val nodeId: String,
        val endpoint: String,
        val chain: ChainService,
        val channel: Channel) {
    var sentHandshakeCount = 0
    var lastSentHandshake: Protocol.HandShake? = null

    fun sendHandshake() {
    }
}