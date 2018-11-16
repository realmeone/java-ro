package one.realme.krot.module.db

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import one.realme.krot.common.lang.measureTimeSeconds
import one.realme.krot.common.db.RocksDatabase
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RocksDatabaseTest {
    private val key = "key".toByteArray()
    private val fakeKey = "fake".toByteArray()
    private val value = "value".toByteArray()
    private val rounds = 300000

    @Test
    fun testConcurrentPut() = runBlocking {
        val db = RocksDatabase("data/test")
        val t = measureTimeSeconds {
            List(rounds) {
                async {
                    db.put(key, value)
                }
            }.forEach { it.join() }
        }
        db.close()
        db.destroy()
        println("$rounds puts use time: $t seconds")
    }

    @Test
    fun testConcurrentGet() = runBlocking {
        val db = RocksDatabase("data/test")
        db.put(key, value)
        val t = measureTimeSeconds {
            List(rounds) {
                async {
                    db.get(key)
                }
            }.forEach { it.join() }
        }
        db.close()
        db.destroy()
        println("$rounds gets use time: $t seconds")
    }

    @Test
    fun testCrud() {
        val db = RocksDatabase("data/test")
        db.put(key, value)
        assertTrue(db.has(key))
        assertFalse(db.has(fakeKey))
        assertArrayEquals(value, db.get(key))

        db.delete(key)
        assertFalse(db.has(key))
        assertFalse(db.has(fakeKey))
        assertEquals(null, db.get(key))
        db.close()
        db.destroy()
    }
}