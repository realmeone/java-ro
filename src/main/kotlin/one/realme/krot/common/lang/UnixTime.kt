package one.realme.krot.common.lang


import java.time.Instant

class UnixTime private constructor(private val value: Int) {
    fun toByteArray(): ByteArray = value.toByteArray()
    fun toInt(): Int = value

    override fun toString(): String {
        return "UnixTime(value=$value)"
    }


    companion object {
        fun fromBytes(bytes: ByteArray): UnixTime = UnixTime(bytes.toInt())
        fun fromMillis(milliSeconds: Long): UnixTime = UnixTime((milliSeconds / 1000).toInt())
        fun now(): UnixTime = UnixTime(Instant.now().epochSecond.toInt())
        fun fromSeconds(seconds: Int): UnixTime = UnixTime(seconds)
    }
}