package one.realme

import com.google.common.io.Files
import com.typesafe.config.ConfigFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.rocksdb.Options
import org.rocksdb.RocksDB
import org.rocksdb.RocksDBException
import java.io.File


class IdeaSet {
    init {
        RocksDB.loadLibrary()
    }

    @ParameterizedTest
    @CsvSource(
            "mainnet.conf, mainnet, 50505",
            "testnet.conf, testnet, 50505"
    )
    fun testTypeSafeConfig(file: String, eName: String, ePort: Int) {
        val env = ConfigFactory.load(file)
        assertEquals(eName, env.getString("name"))
        assertEquals(ePort, env.getInt("net.port"))
    }

    @Test
    fun testRocksDb() {
        val chain = "data/chain"
        val nodes = "data/nodes"
        val options = Options()
        options.setCreateIfMissing(true)

        try {
            Files.createParentDirs(File(chain))
            val chainDb = RocksDB.open(options, chain)
            val nodesDb = RocksDB.open(options, nodes)
            chainDb.put("1".toByteArray(), "test".toByteArray())
            nodesDb.put("1".toByteArray(), "test".toByteArray())
            nodesDb.put("1".toByteArray(), "test".toByteArray())
            chainDb.close()
            nodesDb.close()
        } catch (e: RocksDBException) {
            fail { "Caught the expected exception -- ${e.message}" }
        } finally {
            RocksDB.destroyDB(chain, options)
            RocksDB.destroyDB(nodes, options)
        }

    }
}