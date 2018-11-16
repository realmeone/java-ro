package one.realme.krot.common.codec


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigInteger
import java.util.*

class Base58Test {

    @Test
    fun testBase58Encode() {
        assertEquals("StV1DL6CwTryKyV", Base58.encode("hello world".toByteArray()))
        assertEquals("JxF12TrwUP45BMd", Base58.encode("Hello World".toByteArray()))
        assertEquals("16Ho7Hs", Base58.encode(BigInteger.valueOf(3471844090L).toByteArray()))
        assertEquals("1", Base58.encode(ByteArray(1)))
        assertEquals("111111111", Base58.encode(ByteArray(9)))
        assertEquals("", Base58.encode(ByteArray(0)))
    }

    @Test
    fun testBase58Decode() {
        assertTrue(Arrays.equals(Base58.decode("1"), ByteArray(1)))
        assertTrue(Arrays.equals(Base58.decode("1111"), ByteArray(4)))
        assertEquals("hello world", String(Base58.decode("StV1DL6CwTryKyV")))
        assertEquals(0, Base58.decode("").size.toLong())
    }

    @Test
    fun testInvalidBase58Decode() {
        assertThrows<IllegalArgumentException>("Should throw an exception") {
            Base58.decode("This isn't a valid base58")
        }
    }
}