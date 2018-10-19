package one.realme.db

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class MemDatabase : Database {
    private val db = HashMap<String, ByteArray>()
    private val lock = ReentrantLock()

    override fun put(key: ByteArray, value: ByteArray) =
            lock.withLock {
                db[String(key)] = value
            }


    override fun get(key: ByteArray): ByteArray? =
            lock.withLock {
                return db[String(key)]
            }

    override fun has(key: ByteArray): Boolean = db.contains(String(key))

    override fun delete(key: ByteArray): Unit =
            lock.withLock {
                db.remove(String(key))
            }


    override fun close() =
            lock.withLock {
                db.clear()
            }


}