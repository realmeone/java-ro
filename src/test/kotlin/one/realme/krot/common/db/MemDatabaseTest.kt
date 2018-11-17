package one.realme.krot.common.db

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import one.realme.krot.common.lang.measureTimeSeconds
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MemDatabaseTest {
    private val key = "key".toByteArray()
    private val fakeKey = "fake".toByteArray()
    private val value = "value".toByteArray()
    private val rounds = 200000

    @Test
    fun testConcurrent() = runBlocking {
        val db = MemDatabase()
        val t = measureTimeSeconds {
            List(rounds) {
                async {
                    db.put(key, value)
                }
            }.forEach { it.join() }
        }
        println("$rounds puts use time: $t seconds")
    }

    @Test
    fun testCrud() {
        val db = MemDatabase()
        db.put(key, value)
        assertTrue(db.has(key))
        assertFalse(db.has(fakeKey))
        assertArrayEquals(value, db.get(key))

        db.delete(key)
        assertFalse(db.has(key))
        assertFalse(db.has(fakeKey))
        assertEquals(null, db.get(key))
    }
}