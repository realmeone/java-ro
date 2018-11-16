package one.realme.krot.common.db

interface Database {
    fun put(key: ByteArray, value: ByteArray)
    fun get(key: ByteArray): ByteArray?
    fun has(key: ByteArray): Boolean
    fun delete(key: ByteArray)
    fun close()
    fun destroy()
}