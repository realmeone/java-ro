package one.realme.krot.chain

import one.realme.krot.db.BlockDb
import one.realme.krot.db.RocksDatabase
import java.util.*

class BlockChain(
        val genesisBlock: Block,
        var tailBlock: Block = genesisBlock
) {
    // cache
    private val blocks = Vector<Block>()
    private val txPool = Vector<Transaction>()
    // db
    private val blockDb = BlockDb(RocksDatabase("data/blocks"))

    fun addTransaction(tx: Transaction) {
        // verify tx
        txPool.add(tx)
    }

    fun addBlock(block: Block): BlockHeader {
        // verify block
        blocks.add(block)
        blockDb.putBlock(block)
        return block.header()
    }
}