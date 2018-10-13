package one.realme.crypto.encoding

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigInteger
import java.util.*

class Base58Test {

    @Test(expected = IllegalArgumentException::class)
    fun testBase58() {
        assertEquals("StV1DL6CwTryKyV", Base58.encode("hello world".toByteArray()))
        assertEquals("JxF12TrwUP45BMd", Base58.encode("Hello World".toByteArray()))
        assertEquals("16Ho7Hs", Base58.encode(BigInteger.valueOf(3471844090L).toByteArray()))
        assertEquals("1", Base58.encode(ByteArray(1)))
        assertEquals("111111111", Base58.encode(ByteArray(9)))
        assertEquals("", Base58.encode(ByteArray(0)))

        assertTrue("1", Arrays.equals(Base58.decode("1"), ByteArray(1)))
        assertTrue("1111", Arrays.equals(Base58.decode("1111"), ByteArray(4)))
        assertEquals("hello world", String(Base58.decode("StV1DL6CwTryKyV")))
        assertEquals(0, Base58.decode("").size.toLong())

        // this will throw a IllegalArgumentException
        Base58.decode("This isn't a valid base58")
    }
}