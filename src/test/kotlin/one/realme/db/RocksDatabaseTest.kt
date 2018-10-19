package one.realme.db

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class RocksDatabaseTest {
    private val key = "key".toByteArray()
    private val fakeKey = "fake".toByteArray()
    private val value = "value".toByteArray()
    private val rounds = 200000

    @Test
    fun testConcurrentPut() = runBlocking {
        val db = RocksDatabase("data/test")
        val t = measureTimeMillis {
            List(rounds) {
                async {
                    db.put(key, value)
                }
            }.forEach { it.join() }
        }
        db.close()
        db.destroy()
        println("$rounds puts use time: ${t / 1000.0} seconds")
    }

    @Test
    fun testConcurrentGet() = runBlocking {
        val db = RocksDatabase("data/test")
        db.put(key, value)
        val t = measureTimeMillis {
            List(rounds) {
                async {
                    db.get(key)
                }
            }.forEach { it.join() }
        }
        db.close()
        db.destroy()
        println("$rounds puts use time: ${t / 1000.0} seconds")
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