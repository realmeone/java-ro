package one.realme.db

import org.rocksdb.*
import java.io.File

class RocksDatabase(private val dbPath: String) : Database {
    private val db: RocksDB

    companion object {
        val nodesDbPath = "data/nodes"
        // it will auto create db files
//        val blocksDb: RocksDatabase = RocksDatabase("data/blocks")
//        val nodesDb: RocksDatabase = RocksDatabase("data/nodes")
    }

    init {
        File(dbPath).mkdirs()

        // https://github.com/facebook/rocksdb/wiki/Setup-Options-and-Basic-Tuning
        val options = Options().setCreateIfMissing(true)
                .setWriteBufferSize(64 shl 20) // 64 << 20 , 64MB
                .setMaxWriteBufferNumber(3)
                .setMaxBackgroundCompactions(4)
                .setMaxBackgroundJobs(2)
                .setMaxOpenFiles(512)
                .setBytesPerSync(1048576)
                .setLevelCompactionDynamicLevelBytes(true)

        val tableConfig = BlockBasedTableConfig()
        tableConfig.setBlockSize(16 * 1024)
        tableConfig.setBlockCache(LRUCache(128 shl 20)) // 128 << 20, 128MB
        tableConfig.setCacheIndexAndFilterBlocks(true)
        tableConfig.setPinL0FilterAndIndexBlocksInCache(true)
        tableConfig.setFilter(BloomFilter(10, false))

        options.setTableFormatConfig(tableConfig)

        db = RocksDB.open(options, dbPath)
    }

    override fun put(key: ByteArray, value: ByteArray) = db.put(key, value)

    override fun get(key: ByteArray): ByteArray? = db.get(key)

    override fun has(key: ByteArray): Boolean = get(key) != null

    override fun delete(key: ByteArray) = db.delete(key)

    override fun close() = db.close()

    override fun destroy() = RocksDB.destroyDB(dbPath, Options())

}