package one.realme.krot.service.chain

import one.realme.krot.common.primitive.Block
import one.realme.krot.common.primitive.Hash
import one.realme.krot.common.db.Database
import one.realme.krot.common.db.RocksDatabase

/**
 * Store all confirmed block
 */
internal class BlockDb: RocksDatabase("data/blocks") {

    fun pushBlock(block: Block) {
        put(block.hash.toByteArray(), block.toByteArray())
    }

    fun fetchBlock(hash: Hash): Block? {
        val bytes = get(hash.toByteArray()) ?: return null
        return Block.fromByteArray(bytes)
    }
}