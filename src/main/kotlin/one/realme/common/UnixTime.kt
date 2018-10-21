package one.realme.common

import java.time.Instant

class UnixTime private constructor(private val value: Int) {
    fun toBytes(): ByteArray = value.toBytes()
    fun toBytesLE(): ByteArray = value.toBytesLE()
    fun toInt(): Int = value

    companion object {
        fun fromMillis(milliSeconds: Long): UnixTime = UnixTime((milliSeconds / 1000).toInt())
        fun now(): UnixTime = UnixTime(Instant.now().epochSecond.toInt())
        fun fromSeconds(seconds: Int): UnixTime = UnixTime(seconds)
    }
}