package one.realme.krot.db

import one.realme.krot.chain.Block
import one.realme.krot.chain.Hash

class BlockDb(private val db: Database) {

    fun pushBlock(block: Block) {
        db.put(block.hash.toByteArray(), block.toByteArray())
    }

    fun fetchBlock(hash: Hash): Block? {
        val bytes = db.get(hash.toByteArray()) ?: return null
        return Block.fromByteArray(bytes)
    }
}