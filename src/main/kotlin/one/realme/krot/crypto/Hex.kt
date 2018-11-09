@file:kotlin.jvm.JvmName("HexKt")

package one.realme.krot.crypto

fun ByteArray.toHexString() = Hex.encode(this)
fun Int.toHexString() = toString(16)

object Hex {
    //    private val digits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
    private const val digits = "0123456789abcdef"

    fun decode(str: String): ByteArray {
        val data = str.toCharArray()
        val len = data.size

        if (len and 0x01 != 0) throw IllegalArgumentException("Odd number of characters.")

        val out = ByteArray(len shr 1)

        var i = 0
        var j = 0
        while (j < len) {
            var f = data[j].toDigit() shl 4
            j++
            f = f or data[j].toDigit()
            j++
            out[i] = (f and 0xFF).toByte()
            i++
        }

        return out
    }

    fun encode(data: ByteArray): String {
        val out = CharArray(data.size shl 1)
        for (i in data.indices) {
            val v = data[i].toInt() and 0xff
            out[i shl 1] = digits[v shr 4]
            out[(i shl 1) + 1] = digits[v and 0xf]
        }
        return String(out)
    }

    private fun Char.toDigit() = Character.digit(this, 16).also {
        if (it == -1) throw IllegalArgumentException("Illegal hexadecimal character $this")
    }
}