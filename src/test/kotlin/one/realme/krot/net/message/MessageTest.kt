package one.realme.krot.net.message

import one.realme.krot.common.toBytesLE
import one.realme.krot.crypto.sha256Twice
import one.realme.krot.crypto.toInt
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MessageTest {

    @Test
    fun testMessage() {
        val cmd = Command.ping()
        val payload = ByteArray(0)
        val length = payload.size
        val checksum = payload.sha256Twice().copyOf(4)

        val msg = Message(cmd, payload)

        assertEquals(length, msg.length)
        assertEquals(cmd, msg.command)
        assertArrayEquals(checksum, msg.checksum)
        assertArrayEquals(payload, msg.payload)
    }

    @Test
    fun testEncodeDecode() {
        val cmd = Command.ping()
        val payload = ByteArray(0)
        val length = payload.size
        val checksum = payload.sha256Twice().copyOf(4)

        // MessageEncode
        val data = cmd.code.toBytesLE() +
                length.toBytesLE() +
                checksum.reversedArray() +
                payload.reversedArray()


        assertEquals(4 + 4 + 4 + 0, data.size)

        // MessageDecode
        val cmdByteArray = data.copyOfRange(0, 4)
        val lengthByteArray = data.copyOfRange(4, 8)
        val checksumByteArray = data.copyOfRange(8, 12)
        val payloadByteArray = data.copyOfRange(12, data.size)
        cmdByteArray.reverse()
        lengthByteArray.reverse()
        checksumByteArray.reverse()
        payloadByteArray.reverse()

        assertEquals(cmd.code, cmdByteArray.toInt())
        assertEquals(length, lengthByteArray.toInt())
        assertArrayEquals(checksum, checksumByteArray)
        assertArrayEquals(payload, payloadByteArray)
    }
}