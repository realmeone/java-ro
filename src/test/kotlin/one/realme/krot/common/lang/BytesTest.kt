package one.realme.krot.common.lang

import com.google.common.primitives.Bytes
import one.realme.krot.common.digest.sha256
import one.realme.krot.common.codec.Hex
import one.realme.krot.common.codec.toHexString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer


class BytesTest {
    private val b1 = "b1".toByteArray().sha256()
    private val b2 = "b2".toByteArray().sha256()
    private val rounds = 100_000_000

    private fun measureTime(desc: String, action: () -> ByteArray) {
        val timeUsed = measureTimeSeconds {
            for (i in 1..rounds)
                action()
        }
        println("$desc with $rounds times use time :  $timeUsed seconds")
    }

    @Test
    fun testIntToByteArray() {
        assertEquals("0000000f", 15.toByteArray().toHexString())
        assertEquals("000000ff", 255.toByteArray().toHexString())
        assertEquals("0000ffff", 65535.toByteArray().toHexString())
        assertEquals("7fffffff", Int.MAX_VALUE.toByteArray().toHexString())
    }

    @Test
    fun testLongToByteArray() {
        assertEquals("000000000000000f", 15L.toByteArray().toHexString())
        assertEquals("00000000000000ff", 255L.toByteArray().toHexString())
        assertEquals("000000000000ffff", 65535L.toByteArray().toHexString())
        assertEquals("000000007fffffff", Int.MAX_VALUE.toLong().toByteArray().toHexString())
        assertEquals("7fffffffffffffff", Long.MAX_VALUE.toByteArray().toHexString())
    }

    @Test
    fun testByteArrayToInt() {
        assertEquals(15, Hex.decode("0000000f").toInt())
        assertEquals(255, Hex.decode("000000ff").toInt())
        assertEquals(65535, Hex.decode("0000ffff").toInt())
        assertEquals(Int.MAX_VALUE, Hex.decode("7fffffff").toInt())
    }

    @Test
    @DisplayName("Test concat 4 byte to an array performance")
    fun testBytesConcatWhoIsFaster() {
        measureTime("kotin '+'") {
            b1 + b2 + b1 + b2
        }

        measureTime("guava Bytes.concat ") {
            Bytes.concat(b1, b2, b1, b2)
        }

        measureTime("jvm system.arraycopy") {
            val result = ByteArray((b1.size + b2.size) * 2)
            System.arraycopy(b1, 0, result, 0, b1.size)
            System.arraycopy(b2, 0, result, b1.size, b2.size)
            System.arraycopy(b1, 0, result, b1.size + b2.size, b1.size)
            System.arraycopy(b2, 0, result, b1.size * 2 + b2.size, b2.size)
            result
        }

        measureTime("jvm byteArrayOutputStream") {
            ByteArrayOutputStream().use { outputStream ->
                outputStream.write(b1)
                outputStream.write(b2)
                outputStream.write(b1)
                outputStream.write(b2)
                outputStream.toByteArray()
            }
        }

        measureTime("jvm nio byteBuffer") {
            ByteBuffer.allocate((b1.size + b2.size) * 2)
                    .put(b1)
                    .put(b2)
                    .put(b1)
                    .put(b2)
                    .array()
        }
    }
}