package one.realme.chain

import one.realme.common.UnixTime
import one.realme.common.toBytesLE
import one.realme.crypto.sha256Twice
import java.nio.ByteBuffer
import java.util.*

class Block(
        val version: Int,
        val height: Long,
        val prevBlockHash: Hash,
        val timestamp: UnixTime
) {
    private val transactions = Vector<Transaction>()
    private var merkleRootHash: Hash = Hash.empty()
    private var hash: Hash = Hash.empty()// this block hash

    fun header(): BlockHeader {
        return BlockHeader(
                version = version,
                prevBlockHash = prevBlockHash,
                merkleRootHash = merkleRootHash(),
                time = timestamp
        )
    }

    fun merkleRootHash(): Hash {
        if (merkleRootHash.isEmpty())
            merkleRootHash = Merkle.merkleTreeRoot(transactions.map { it.hash() }.toList())
        return merkleRootHash
    }

    fun hash(): Hash {
        if (hash.isEmpty()) {
            val bytes = ByteBuffer.allocate(72)
                    .put(version.toBytesLE()) // int 4 bytes
                    .put(prevBlockHash.toBytesLE()) // hash 32 bytes
                    .put(merkleRootHash().toBytesLE()) // hash 32 bytes
                    .put(timestamp.toBytesLE()) // time 4 bytes
                    .array()
            hash = Hash.fromBytes(bytes.sha256Twice().reversedArray())
        }
        return hash
    }


    override fun toString(): String {
        return String.format(
                "Block(height=%d, hash=%s, ver=0x%08x, prevBlockHash=%s, merkleRoot=%s, time=%d)\n",
                height,
                hash(),
                version,
                prevBlockHash,
                merkleRootHash(),
                timestamp.toInt()
        )
    }
}