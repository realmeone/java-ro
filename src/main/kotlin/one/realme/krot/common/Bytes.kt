@file:kotlin.jvm.JvmName("BytesKt")

package one.realme.krot.common

//fun Int.toByteArray() = Ints.toByteArray(this)
fun Int.toByteArray() = ByteArray(4) {
    (this shr 8 * (3 - it) and 0xff).toByte()
}

//fun Long.toByteArray() = Longs.toByteArray(this)
fun Long.toByteArray() = ByteArray(8) {
    (this shr 8 * (7 - it) and 0xffL).toByte()
}

//fun ByteArray.toInt() = Ints.fromByteArray(this)
fun ByteArray.toInt() = this[0].toInt() shl 24 or
        (this[1].toInt() and 0xff shl 16) or
        (this[2].toInt() and 0xff shl 8) or
        (this[3].toInt() and 0xff)

fun ByteArray.toLong() = this[0].toLong() shl 56 or
        (this[1].toLong() and 0xffL shl 48) or
        (this[2].toLong() and 0xffL shl 40) or
        (this[3].toLong() and 0xffL shl 32) or
        (this[4].toLong() and 0xffL shl 24) or
        (this[5].toLong() and 0xffL shl 16) or
        (this[6].toLong() and 0xffL shl 8) or
        (this[7].toLong() and 0xffL)