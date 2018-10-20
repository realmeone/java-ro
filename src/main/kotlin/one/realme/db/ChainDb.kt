package one.realme.db

import one.realme.chain.Block

object ChainDb {
    private const val chainDbPath = "data/chain"
    private val db = RocksDatabase(chainDbPath)

    fun putBlock(block: Block) {
        db.put(block.hash().toBytes(), block.hash().toBytes())
    }

//
//    fun getBlock(hash: String) : Block {
//        rocksdb.get(Hash.fromString(hash).toBytes())
//    }
}