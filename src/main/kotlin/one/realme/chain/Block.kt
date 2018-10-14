package one.realme.chain

import java.util.*

class Block(
        val version: Int,
        val height: Long,
        val prevBlockHash: Hash,
        val timestamp: Long
) {
    private val transactions = Vector<Transaction>()
    private var merkleRootHash: Hash = Hash.empty()
    private var hash: Hash = Hash.empty()// this block hash

    fun header(): BlockHeader {
        return BlockHeader(
                version = version,
                prevBlockHash = prevBlockHash,
                merkleRootHash = merkleRootHash(),
                time = timestamp,
                nonce = transactions.size
        )
    }

    fun merkleRootHash(): Hash {
        if (merkleRootHash.isEmpty())
            merkleRootHash = Merkle.merkleTreeRoot(transactions.map { it.hash() }.toList())
        return merkleRootHash
    }

    fun hash(): Hash {
//        if (hash.isEmpty())
//          hash = Hash.fromObject(this)  ??? Hash.fromBytes(byteArrayOf(b1, b2, b3, b4))
        return hash
    }


    override fun toString(): String {
        return String.format(
                "Block(height=%d, hash=%s, ver=0x%08x, prevBlockHash=%s, prevMerkleRoot=%s, time=%u)\n",
                height,
                hash(),
                version,
                prevBlockHash,
                merkleRootHash(),
                timestamp
        )
    }
}