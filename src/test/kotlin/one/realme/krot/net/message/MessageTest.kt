package one.realme.krot.net.message

import one.realme.krot.crypto.digest.sha256Twice
import one.realme.krot.net.romtp.Message
import one.realme.krot.net.romtp.MessageType
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MessageTest {

    @Test
    fun testMessage() {
        val type = MessageType.PING
        val content = ByteArray(0)
        val length = content.size
        val checksum = content.sha256Twice().copyOf(4)

        val msg = Message(type = type, content = content)

        assertEquals(length, msg.contentLength)
        assertEquals(type, msg.type)
        assertArrayEquals(checksum, msg.contentChecksum)
        assertArrayEquals(content, msg.content)
    }
//
//    @Test
//    fun testEncodeDecode() {
//        val type = MessageType.PING
//        val content = ByteArray(0)
//        val length = content.size
//        val checksum = content.sha256Twice().copyOf(4)
//
//        // MessageEncode
//        val data = cmd.toByteArrayLE() +
//                length.toByteArrayLE() +
//                checksum.reversedArray() +
//                payload.reversedArray()
//
//
//        assertEquals(4 + 4 + 4 + 0, data.size)
//
//        // MessageDecode
//        val cmdByteArray = data.copyOfRange(0, 4)
//        val lengthByteArray = data.copyOfRange(4, 8)
//        val checksumByteArray = data.copyOfRange(8, 12)
//        val payloadByteArray = data.copyOfRange(12, data.size)
//        cmdByteArray.reverse()
//        lengthByteArray.reverse()
//        checksumByteArray.reverse()
//        payloadByteArray.reverse()
//
//        assertEquals(cmd.code, cmdByteArray.toInt())
//        assertEquals(length, lengthByteArray.toInt())
//        assertArrayEquals(checksum, checksumByteArray)
//        assertArrayEquals(payload, payloadByteArray)
//    }
}