package one.realme.chain

import one.realme.db.RocksDatabase
import java.util.*

class BlockChain(
        val genesisBlock: Block,
        var tailBlock: Block = genesisBlock
) {
    private val blocks = Vector<Block>()
    private val txPool = Vector<Transaction>()
    private val db = RocksDatabase(RocksDatabase.chainDbPath)

    fun addBlock(block: Block): BlockHeader {
        blocks.add(block)
        return block.header()
    }
}