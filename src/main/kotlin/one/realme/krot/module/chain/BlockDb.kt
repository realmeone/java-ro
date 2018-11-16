package one.realme.krot.module.chain

import one.realme.krot.common.primitive.Block
import one.realme.krot.common.primitive.Hash
import one.realme.krot.common.db.Database

class BlockDb(private val db: Database) {

    fun pushBlock(block: Block) {
        db.put(block.hash.toByteArray(), block.toByteArray())
    }

    fun fetchBlock(hash: Hash): Block? {
        val bytes = db.get(hash.toByteArray()) ?: return null
        return Block.fromByteArray(bytes)
    }
}