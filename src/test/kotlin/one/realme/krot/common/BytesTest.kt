package one.realme.krot.common

import com.google.common.primitives.Bytes
import one.realme.krot.crypto.Digests
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer


class BytesTest {
    private val b1 = Digests.sha256("b1".toByteArray())
    private val b2 = Digests.sha256("b2".toByteArray())
    private val rounds = 10_000_000

    private fun measureTime(desc: String, action: () -> ByteArray) {
        val timeUsed = measureTimeSeconds {
            for (i in 1..rounds)
                action()
        }
        println("$desc with $rounds times use time :  $timeUsed seconds")
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