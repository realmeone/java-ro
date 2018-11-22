package one.realme.krot.service.net.romtp

import one.realme.krot.common.lang.UnixTime
import one.realme.krot.service.net.romtp.content.Inv
import one.realme.krot.service.net.romtp.content.Ping
import one.realme.krot.service.net.romtp.content.Pong

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
        fun getTime() = Message(type = MessageType.GET_TIME)
        fun ping() = Message(type = MessageType.PING, content = Ping().toByteArray())
        fun pong() = Message(type = MessageType.PING, content = Pong().toByteArray())
        fun time() = Message(type = MessageType.TIME, content = UnixTime.now().toByteArray())
//        fun version(height: Long) = Message(type = MessageType.VERSION,
//                content = Ver(
//                        Version.CURRENT,
//                        UnixTime.now().toInt()
//                ))
    }
}