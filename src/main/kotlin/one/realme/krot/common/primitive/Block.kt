package one.realme.krot.common.primitive

import one.realme.krot.common.Version
import one.realme.krot.common.digest.sha256Twice
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.common.lang.toByteArray
import one.realme.krot.common.lang.toInt
import one.realme.krot.common.lang.toLong
import java.nio.ByteBuffer
import java.util.*

class Block(
        val version: Int = Version.CURRENT,
        val height: Long = -1,
        val prevBlockHash: Hash = Hash.empty(),
        val timestamp: UnixTime = UnixTime.now()
//        val producer: Address
) {
    companion object {

        fun fromByteArray(bytes: ByteArray): Block = Block(
                bytes.copyOfRange(0, 4).toInt(),
                bytes.copyOfRange(4, 12).toLong(),
                Hash.fromBytes(bytes.copyOfRange(12, 44)),
                UnixTime.fromSeconds(bytes.copyOfRange(76, 80).toInt())
        ).also {
            it.merkleRootHash = Hash.fromBytes(bytes.copyOfRange(44, 76))
        }

    }

    val transactions = Vector<Transaction>()
    var merkleRootHash: Hash = Hash.empty()
        get() {
            if (Hash.empty() == field)
                field = Merkle.merkleTreeRoot(transactions.map { it.hash }.toList())
            return field
        }

    val hash by lazy {
        Hash.fromBytes(toByteArray().sha256Twice())
    }

    fun toByteArray(): ByteArray = ByteBuffer.allocate(80)
            .put(version.toByteArray()) // int 4 bytes
            .put(height.toByteArray()) // long 8 bytes
            .put(prevBlockHash.toByteArray()) // hash 32 bytes
            .put(merkleRootHash.toByteArray()) // hash 32 bytes
            .put(timestamp.toByteArray()) // time 4 bytes
            .array()

    fun header(): BlockHeader =
            BlockHeader(
                    version,
                    prevBlockHash,
                    merkleRootHash,
                    timestamp
            )

    override fun toString(): String =
            String.format(
                    "Block(height=%d, hash=%s, ver=0x%08x, prevBlockHash=%s, merkleRoot=%s, time=%d)\n",
                    height,
                    hash,
                    version,
                    prevBlockHash,
                    merkleRootHash,
                    timestamp.toInt()
            )

}