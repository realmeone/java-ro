package one.realme.krot.net.romtp

import one.realme.krot.common.UnixTime
import one.realme.krot.net.romtp.body.Inv
import one.realme.krot.net.romtp.header.MessageType
import java.util.*

/**
 * Message Structure
 *
 * |version|type|content-checksum|content-length|content|
 */
class Message(
        val version: Int = 0x01, // 4 bytes
        val type: MessageType, // 4 bytes
        val content: ByteArray = ByteArray(0), // the actual data
        val length: Int = content.size // 4 bytes,  Length of content in number of bytes
) {

    companion object {
        // static message
        val HELLO = Message(type = MessageType.HELLO)
        val DISCONNECT = Message(type = MessageType.DISCONNECT)
        val PING = Message(type = MessageType.PING)
        val PONG = Message(type = MessageType.PONG)
        val GET_TIME = Message(type = MessageType.GET_TIME)
        // instance message
        fun time() = Message(type = MessageType.TIME, content = UnixTime.now().toByteArray())
        fun inv(inv: Inv) = Message(type = MessageType.INV, content = inv.toByteArray())
    }

    override fun toString(): String {
        return "Message(type=$type, content=${Arrays.toString(content)}, length=$length)"
    }
}