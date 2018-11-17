package one.realme.krot.service.net.romtp.content

import one.realme.krot.common.primitive.Hash

/**
 * when a peer client connect to server, send this message, and will receive ver.
 * No further communication is possible until both peers have exchanged their ver.
 */
class HandShake(
        val version: Int, // 4bytes
        val timestamp: Int, // 4 bytes
        val nodeId: Hash, // 32 bytes, The node identity
        val addr: NetAddr, // 26 bytes, The network address from this message sender
        val height: Long, // 	8 bytes, The last block received by the emitting node
        val os: String, // runtime os
        val agent: String // runtime instance name
) {
}