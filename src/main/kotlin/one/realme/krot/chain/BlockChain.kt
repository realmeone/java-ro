package one.realme.krot.chain

import one.realme.krot.db.BlockDb
import one.realme.krot.db.RocksDatabase
import java.util.*

class BlockChain(
        val genesisBlock: Block,
        var tailBlock: Block = genesisBlock
) {
    // cache
    private val txPool = Vector<Transaction>()
    //    private val tailBlocks = HashMap<Hash, Block>() // tailBlocks may flush deferred
    // db
    private val blockDb = BlockDb(RocksDatabase("data/blocks"))

    fun addTransaction(tx: Transaction) {
        if (tx.isValid() && !txPool.contains(tx)) txPool.add(tx)
    }

    fun push(block: Block): BlockHeader {
        // verify block
        blockDb.pushBlock(block)
        tailBlock = block
        return block.header()
    }

    fun slice(indices: LongRange): List<Block> {
        val blocks = mutableListOf(tailBlock)
        if (indices.last > 0) {
            var current: Block = blocks[0]
            while (current.height > indices.first && genesisBlock != current) {
                current = blockDb.fetchBlock(current.prevBlockHash) ?: genesisBlock
                blocks.add(current)
            }
        }
        return blocks
    }
}