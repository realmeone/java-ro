package one.realme.chain

import com.google.common.primitives.Ints
import one.realme.common.Digests
import one.realme.common.toHexString
import one.realme.crypto.encoding.Hex
import java.util.*

/**
 * maybe hash32 is the right name?
 * restrict bytes length to 32?
 *
 * bytes will store in big-endian
 */
class Hash private constructor(private val bytes: ByteArray) {
    fun bits(): Int = bytes.size * 8
    fun asBytes(): ByteArray = bytes.clone()
    fun asIntBE(): Int = Ints.fromByteArray(bytes)
    fun asIntLE(): Int = Ints.fromByteArray(bytes.reversedArray())

    override fun toString(): String = asBytes().toHexString()
    override fun hashCode(): Int = asIntBE()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Hash
        if (!bytes.contentEquals(other.bytes)) return false
        return true
    }

    fun isEmpty(): Boolean = ByteArray(32).contentEquals(bytes)

    companion object {
        fun empty() = fromBytes(ByteArray(32))
        fun fromBytes(bytes: ByteArray): Hash = Hash(bytes)
        fun fromString(hex: String): Hash = Hash(Hex.decode(hex))

        /**
         * Merkle
         *
         * ABCDEEEE .......Merkle root
         * /        \
         * ABCD        EEEE
         * /    \      /
         * AB    CD    EE .......E is paired with itself
         * /  \  /  \  /
         * A  B  C  D  E .........Transactions Hex Data
         *
         * // TODO hex string ,not hash?
         */
        fun merkleTreeRoot(hashList: List<Hash>): Hash = buildMerkleTree(hashList)[0]

        fun buildMerkleTree(hashList: List<Hash>): List<Hash> {
            require(hashList.isNotEmpty())
            // in JVM bytes in big-endian, here we do little-endian
            var hashList = hashList
            while (hashList.size > 1) {
                val remainder = hashList.size % 2
                val tempHashList = ArrayList<Hash>()
                var i = 0
                while (i < hashList.size - 1) {
                    tempHashList.add(
                        Hash.fromBytes(
                            Digests.sha256(
                                hashList[hashList.size - 1].asBytes().reversedArray(),
                                hashList[hashList.size - 1].asBytes().reversedArray()
                            ).reversedArray()
                        )
                    )
                    i += 2
                }

                if (remainder == 1) {
                    //
                    tempHashList.add(
                        Hash.fromBytes(
                            Digests.sha256(
                                hashList[hashList.size - 1].asBytes().reversedArray(),
                                hashList[hashList.size - 1].asBytes().reversedArray()
                            ).reversedArray()
                        )
                    )
                }
                hashList = tempHashList
            }

            return hashList// only root left, return it
        }
    }
}