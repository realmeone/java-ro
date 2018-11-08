package one.realme.krot.chain

import one.realme.krot.common.UnixTime
import one.realme.krot.common.Version
import one.realme.krot.crypto.digest.sha256Twice
import java.nio.ByteBuffer
import java.util.*

class Block(
        val version: Int = Version.CURRENT,
        val height: Long = -1,
        val prevBlockHash: Hash = Hash.empty(),
        val timestamp: UnixTime = UnixTime.now()
) {
    private val transactions = Vector<Transaction>()

    val merkleRootHash by lazy {
        Merkle.merkleTreeRoot(transactions.map { it.hash() }.toList())
    }

    val hash by lazy {
        val bytes = ByteBuffer.allocate(72)
                .put(version.toByte()) // int 4 bytes
                .put(prevBlockHash.toByteArray()) // hash 32 bytes
                .put(merkleRootHash.toByteArray()) // hash 32 bytes
                .put(timestamp.toByteArray()) // time 4 bytes
                .array()
        Hash.fromBytes(bytes.sha256Twice())
    }

    fun header(): BlockHeader {
        return BlockHeader(
                version = version,
                prevBlockHash = prevBlockHash,
                merkleRootHash = merkleRootHash,
                time = timestamp
        )
    }

    override fun toString(): String {
        return String.format(
                "Block(height=%d, hash=%s, ver=0x%08x, prevBlockHash=%s, merkleRoot=%s, time=%d)\n",
                height,
                hash,
                version,
                prevBlockHash,
                merkleRootHash,
                timestamp.toInt()
        )
    }
}