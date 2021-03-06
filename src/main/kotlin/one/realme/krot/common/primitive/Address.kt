package one.realme.krot.common.primitive

import one.realme.krot.common.codec.Base58
import one.realme.krot.common.codec.Hex
import one.realme.krot.common.digest.ripemd160
import one.realme.krot.common.digest.sha256
import one.realme.krot.common.digest.sha256Twice
import java.nio.ByteBuffer
import java.util.*


/**
 *
 * base58(prefix + base58(ripemd160(sha256(pubkey))) + String(sha256twice(CURRENT_VERSION + data), 0, 4)
 */
class Address {
    private val bytes: ByteArray

    companion object {
        fun empty(): Address = Address(ByteArray(25))
    }

    private constructor(rawBytes: ByteArray) {
        bytes = rawBytes
    }

    constructor(base58: String) {
        bytes = Base58.decode(base58)
    }

    constructor(rawPublicKey: String, prefix: Byte) {
        val hash160 = Hex.decode(rawPublicKey).sha256().ripemd160()
        val addr = ByteBuffer.allocate(21).put(prefix).put(hash160).array()
        val checksum = Arrays.copyOf(addr.sha256Twice(), 4)
        bytes = addr + checksum
    }

    fun toHash160(): String = Hex.encode(bytes.copyOfRange(1, 21))
    fun toByteArray(): ByteArray = bytes.clone()

    override fun toString(): String = Base58.encode(bytes)

    fun isValid(): Boolean {
        val checksum = bytes.copyOfRange(21, bytes.size)
        val addr = bytes.copyOfRange(0, 21)
        val toVerify = addr.sha256Twice().copyOf(4)
        return checksum.contentEquals(toVerify)
    }


}