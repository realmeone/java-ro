package one.realme.chain

data class BlockHeader(
    val version: Int = 0,
    val prevBlockHash: Hash,
    val merkleRootHash: Hash,
    val time: Long = 0L,
    val nonce: Int = 0
)