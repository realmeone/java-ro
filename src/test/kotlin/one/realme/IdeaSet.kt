package one.realme

import com.typesafe.config.ConfigFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.rocksdb.Options
import org.rocksdb.RocksDB
import org.rocksdb.RocksDBException


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
        try {
            val options = Options()
            val db = RocksDB.open(options, "data")
            options.createIfMissing()
            db.put("test".toByteArray(), "test".toByteArray())
            db.close()
            RocksDB.destroyDB("data", options)
        } catch (e: RocksDBException) {
            fail { "Caught the expected exception -- ${e.message}" }
        }

    }
}