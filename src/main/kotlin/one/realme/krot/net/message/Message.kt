package one.realme.krot.net.message

import one.realme.krot.common.UnixTime
import one.realme.krot.crypto.sha256Twice
import java.util.*

/**
 * Peer Message
 *
 * Almost all integers are encoded in little  endian.Only IP or port number are encoded big endian.
 * | command(4) | length(4) | checksum(4) | payload(?) |
 */
class Message(
        val command: Command, // 4 bytes
        val payload: ByteArray = ByteArray(0), // the actual data
        val checksum: ByteArray = payload.sha256Twice().copyOf(4), // First 4 bytes of sha256(sha256(payload))
        val length: Int = payload.size // 4 bytes,  Length of payload in number of bytes
) {

    companion object {
        // const message
        val HELLO = Message(Command.HELLO)
        val DISCONNECT = Message(Command.DISCONNECT)
        val PING = Message(Command.PING)
        val PONG = Message(Command.PONG)
        val GET_TIME = Message(Command.GET_TIME)
        // instance message
        fun timeNow() = Message(Command.TIME_NOW, UnixTime.now().toBytes())
    }

    override fun toString(): String {
        return "Message(command=$command, payload=${Arrays.toString(payload)}, checksum=$checksum, length=$length)"
    }


}