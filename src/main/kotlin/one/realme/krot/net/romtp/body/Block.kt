package one.realme.krot.net.romtp.body

class Block(
        val version: Int = 1,
        val prevBlockHash: ByteArray,
        val merkleRootHash: ByteArray,
        val timestamp: Int,
        val coinbase: Coinbase,
        val transactions: List<Transaction>
) {}