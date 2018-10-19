package one.realme.db

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class MemDatabaseTest {
    private val key = "key".toByteArray()
    private val fakeKey = "fake".toByteArray()
    private val value = "value".toByteArray()
    private val rounds = 2000000

    @Test
    fun testConcurrent() {
        // sequence diff result diff?
        // almost the same time
        val db = MemDatabase()
        val t = measureTimeMillis {
            runBlocking {

                List(rounds) {
                    async {
                        db.put(key, value)
                    }
                }.forEach { it.join() }
            }
        }
        println("$rounds puts use time: ${t / 1000.0} seconds")
    }

    @Test
    fun testCrud() = runBlocking {
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