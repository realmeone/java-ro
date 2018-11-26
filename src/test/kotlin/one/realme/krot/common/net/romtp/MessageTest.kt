package one.realme.krot.common.net.romtp

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

class MessageTest {

    @Test
    fun testMessage() {
        val type = MessageType.PING
        val content = ByteArray(0)
        val length = content.size

        val msg = Message(type = type, content = content)
        assertEquals(length, msg.length)
        assertEquals(type, msg.type)
        assertArrayEquals(content, msg.content)
    }

    @Test
    fun testZlibAndUnzlib() {
        val bytes = "12345678".toByteArray()
        val cprssed = bytes.zlib()
        Assertions.assertArrayEquals(bytes, cprssed.unzlib())
    }


    private fun ByteArray.unzlib(): ByteArray {
        val data = this
        val inflater = Inflater()
        inflater.setInput(data)
        return ByteArrayOutputStream(data.size).use {
            val buffer = ByteArray(1024)
            while (!inflater.finished()) {
                val count = inflater.inflate(buffer)
                it.write(buffer, 0, count)
            }
            inflater.end()
            it.toByteArray()
        }
    }

    private fun ByteArray.zlib(): ByteArray {
        val data = this
        val deflater = Deflater()
        deflater.setInput(data)
        return ByteArrayOutputStream(data.size).use {
            val buffer = ByteArray(1024)
            deflater.finish()
            while (!deflater.finished()) {
                val count = deflater.deflate(buffer) // returns the generated code... index
                it.write(buffer, 0, count)
            }
            deflater.end()
            it.toByteArray()
        }
    }
}