package one.realme.krot.net.romtp

import one.realme.krot.common.UnixTime
import one.realme.krot.crypto.digest.sha256Twice
import java.util.*

/**
 * Message Structure
 *
 * |version|type|content-checksum|content-type|content-length|content|
 */
class Message(
        val version: Int = 0x01, // 4 bytes
        val type: MessageType, // 4 bytes
        val contentType: ContentType = ContentType.BINARY, // 4 bytes
        val content: ByteArray = ByteArray(0), // the actual data
        val contentChecksum: ByteArray = content.sha256Twice().copyOf(4), // First 4 bytes of sha256(sha256(content))
        val contentLength: Int = content.size // 4 bytes,  Length of content in number of bytes
) {

    companion object {
        // static message
        val HELLO = Message(type = MessageType.HELLO)
        val DISCONNECT = Message(type = MessageType.DISCONNECT)
        val PING = Message(type = MessageType.PING)
        val PONG = Message(type = MessageType.PONG)
        val GET_TIME = Message(type = MessageType.GET_TIME)
        // instance message
        fun time() = Message(type = MessageType.TIME, content = UnixTime.now().toBytes())
    }

    override fun toString(): String {
        return "Message(type=$type, content=${Arrays.toString(content)}, contentChecksum=$contentChecksum, contentLength=$contentLength)"
    }


}