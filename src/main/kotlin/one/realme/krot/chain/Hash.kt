package one.realme.krot.chain

import com.google.common.primitives.Ints
import one.realme.krot.crypto.encoding.Hex
import one.realme.krot.crypto.digest.toHexString

/**
 * maybe hash32 is the right name?
 * restrict bytes length to 32?
 *
 * bytes will store in big-endian
 */
class Hash private constructor(private val bytes: ByteArray) {
    fun bits(): Int = bytes.size * 8
    fun toByteArray(): ByteArray = bytes.clone()
    fun toByteArrayLE(): ByteArray = bytes.reversedArray()
    fun toInt(): Int = Ints.fromByteArray(bytes)

    override fun toString(): String = toByteArray().toHexString()
    override fun hashCode(): Int = toInt()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Hash
        if (!bytes.contentEquals(other.bytes)) return false
        return true
    }

    fun isEmpty(): Boolean = ByteArray(32).contentEquals(bytes)

    companion object {
        fun empty() = Hash.fromBytes(ByteArray(32))
        fun fromBytes(bytes: ByteArray): Hash = Hash(bytes)
        fun fromString(hex: String): Hash = Hash(Hex.decode(hex))
    }
}