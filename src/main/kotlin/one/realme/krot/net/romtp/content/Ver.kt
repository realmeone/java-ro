package one.realme.krot.net.romtp.content

/**
 * when a peer client connect to server, send this message, and will receive ver.
 * No further communication is possible until both peers have exchanged their ver.
 */
class Ver(
        val version: Int, // 4bytes
        val timestamp: Int, // 4 bytes
        val addrRecv: NetAddr, // 26 bytes, The network address of the node receiving this message
        val addrFrom: NetAddr, // 26 bytes, The network address of the node emitting this message
        val nonce: Long, // 8 bytes, Node random nonce, randomly generated every time a version packet is sent. This nonce is used to detect connections to self
        val startHeight: Long // 	8 bytes, The last block received by the emitting node
) {
}