package one.realme.krot.common.net.romtp

import one.realme.krot.common.lang.UnixTime
import one.realme.krot.common.lang.toByteArray
import one.realme.krot.common.lang.toInt
import one.realme.krot.common.net.romtp.content.Ping
import one.realme.krot.common.net.romtp.content.Pong
import one.realme.krot.net.Protocol
import kotlin.random.Random

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
        private val VERSION_RANGE = IntRange(0, 4)
        private val TYPE_RANGE = IntRange(4, 8)
        private val LENGTH_RANGE = IntRange(8, 12)
        private const val CONTENT_RANGE_START = 12

        fun getTime() = Message(type = MessageType.GET_TIME)
        fun ping() = Message(type = MessageType.PING, content = Protocol.Ping.newBuilder().apply { nonce = Random.nextLong() }.build().toByteArray())
        fun pong() = Message(type = MessageType.PONG, content = Protocol.Pong.newBuilder().apply { nonce = Random.nextLong() }.build().toByteArray())
        fun time() = Message(type = MessageType.TIME, content = UnixTime.now().toByteArray())

        fun fromByteArray(data: ByteArray): Message = with(data) {
            val version = copyOfRange(VERSION_RANGE.start, VERSION_RANGE.last)
            val type = copyOfRange(TYPE_RANGE.start, TYPE_RANGE.last)
            val contentLength = copyOfRange(LENGTH_RANGE.start, LENGTH_RANGE.last)
            val content = if (contentLength.toInt() == 0) ByteArray(0) else copyOfRange(CONTENT_RANGE_START, size)

            Message(version.toInt(), MessageType.ofCode(type.toInt()), content)
        }
    }

    fun toByteArray(): ByteArray = version.toByteArray() +
            type.toByteArray() +
            length.toByteArray() +
            content
}