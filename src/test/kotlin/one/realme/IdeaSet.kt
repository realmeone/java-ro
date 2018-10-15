package one.realme


import com.google.common.base.Stopwatch
import com.google.common.primitives.Bytes
import one.realme.crypto.Digests
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit


class IdeaSet {

    @Test
    fun testBytesConcatWhoIsFaster() {
        val b1 = Digests.sha256("b1".toByteArray())
        val b2 = Digests.sha256("b2".toByteArray())

        val rounds = 10_000_000
        println("test for bytes concat 4 byte array in rounds: $rounds")
        val watch = Stopwatch.createStarted()
        for (i in 1..rounds) {
            b1 + b2 + b1 + b2
        }
        watch.stop()
        println("kotlin concat with operator '+' use time :  ${watch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")

        watch.reset()
        watch.start()
        for (i in 1..rounds) {
            Bytes.concat(b1, b2, b1, b2)
        }
        watch.stop()
        println("guava Bytes.concat use time :  ${watch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")

        watch.reset()
        watch.start()
        for (i in 1..rounds) {
            val result = ByteArray((b1.size + b2.size) * 2)
            System.arraycopy(b1, 0, result, 0, b1.size)
            System.arraycopy(b2, 0, result, b1.size, b2.size)
            System.arraycopy(b1, 0, result, b1.size + b2.size, b1.size)
            System.arraycopy(b2, 0, result, b1.size * 2 + b2.size, b2.size)
        }
        watch.stop()
        println("jvm system.arraycopy use time :  ${watch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")

        watch.reset()
        watch.start()
        for (i in 1..rounds) {
            val outputStream = ByteArrayOutputStream()
            outputStream.write(b1)
            outputStream.write(b2)
            outputStream.toByteArray()
            outputStream.write(b1)
            outputStream.write(b2)
            outputStream.close()
        }
        watch.stop()
        println("jvm byteArrayOutputStream use time :  ${watch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")

        watch.reset()
        watch.start()
        for (i in 1..rounds) {
            ByteBuffer.allocate((b1.size + b2.size) * 2)
                    .put(b1)
                    .put(b2)
                    .put(b1)
                    .put(b2)
                    .array()
        }
        watch.stop()
        println("jvm nio byteBuffer  use time :  ${watch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")
    }
}