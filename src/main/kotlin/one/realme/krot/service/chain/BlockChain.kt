package one.realme.krot.service.chain

import one.realme.krot.common.primitive.Block
import one.realme.krot.common.primitive.BlockHeader
import one.realme.krot.common.primitive.Transaction
import java.util.*

/**
 *
 */
internal class BlockChain(
        val genesisBlock: Block,
        var tailBlock: Block = genesisBlock,
        private val blockDb: BlockDb = BlockDb()
) {
    private val txPool = Vector<Transaction>()

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