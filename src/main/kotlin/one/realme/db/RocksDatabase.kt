package one.realme.db

import org.rocksdb.Options
import org.rocksdb.RocksDB
import java.io.File

class RocksDatabase(private val dbPath: String) : Database {
    private val db: RocksDB

    companion object {
        // it will auto create db files
//        val blocksDb: RocksDatabase = RocksDatabase("data/blocks")
//        val nodesDb: RocksDatabase = RocksDatabase("data/nodes")
    }

    init {
        File(dbPath).mkdirs()
        val options = Options().setCreateIfMissing(true)
        db = RocksDB.open(options, dbPath)
    }

    override fun put(key: ByteArray, value: ByteArray) = db.put(key, value)

    override fun get(key: ByteArray): ByteArray? = db.get(key)

    override fun has(key: ByteArray): Boolean = get(key) != null

    override fun delete(key: ByteArray) = db.delete(key)

    override fun close() = db.close()

    override fun destroy() = RocksDB.destroyDB(dbPath, Options())

}