package one.realme.krot.net.message

import one.realme.krot.crypto.sha256Twice
import one.realme.krot.crypto.toInt
import java.util.*

/**
 * Net message
 *
 * Almost all integers are encoded in little  endian.Only IP or port number are encoded big endian.
 * | command(4) | length(4) | checksum(4) | payload(?) |
 */
open class Message(
        val command: Command, // 4 bytes
        val payload: ByteArray, // the actual data
        val checksum: ByteArray = payload.sha256Twice().copyOf(4), // First 4 bytes of sha256(sha256(payload))
        val length: Int = payload.size // 4 bytes,  Length of payload in number of bytes
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (!Arrays.equals(payload, other.payload)) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(payload)
    }

    override fun toString(): String {
        return "Message(command=$command, payload=${Arrays.toString(payload)}, checksum=$checksum, length=$length)"
    }


}