package one.realme.krot.db

import one.realme.krot.chain.Block

class BlockDb(private val db: Database) {

    fun putBlock(block: Block) {
        db.put(block.hash.toByteArray(), block.hash.toByteArray())
    }

//
//    fun getBlock(hash: String) : Block {
//        rocksdb.get(Hash.fromString(hash).toByteArray())
//    }
}