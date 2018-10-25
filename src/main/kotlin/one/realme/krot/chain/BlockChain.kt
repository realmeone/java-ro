package one.realme.krot.chain

import java.util.*

class BlockChain(
        val genesisBlock: Block,
        var tailBlock: Block = genesisBlock
) {
    private val blocks = Vector<Block>()
    private val txPool = Vector<Transaction>()

    fun addBlock(block: Block): BlockHeader {
        blocks.add(block)
        return block.header()
    }
}