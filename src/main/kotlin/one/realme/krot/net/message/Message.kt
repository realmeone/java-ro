package one.realme.krot.net.message

import java.util.*

/**
 * Net message
 */
data class Message(val payload: ByteArray) {
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
        return String(payload)
    }
}