package one.realme.crypto.encoding

import org.junit.Assert.*
import org.junit.Test
import java.lang.Exception

/**
 * test cases from https://tools.ietf.org/html/rfc4648#page-10
 * see Page 12. Test Vectors Base16
 *
 * why not use guava BaseEncoding.base16 to do Hex thing?
 * guava base16 cannot pass the test cases "testDecoding",
 * it require UpperCase or LowerCase, so we implements one
 */
class HexTest {
    private val cases = arrayOf(
            arrayOf("".toByteArray(), ""),
            arrayOf("1".toByteArray(), "31"),
            arrayOf("A".toByteArray(), "41"),
            arrayOf("f".toByteArray(), "66"),
            arrayOf("fo".toByteArray(), "666f"),
            arrayOf("foo".toByteArray(), "666f6f"),
            arrayOf("foob".toByteArray(), "666f6f62"),
            arrayOf("fooba".toByteArray(), "666f6f6261"),
            arrayOf("foobar".toByteArray(), "666f6f626172"),
            arrayOf("Hello World".toByteArray(), "48656C6C6F20576F726C64"),
            arrayOf(ByteArray(1) { 10 }, "0a"),
            arrayOf(ByteArray(1) { 255.toByte() }, "ff"),
            arrayOf(ByteArray(1), "00"),
            arrayOf(ByteArray(2), "0000"),
            arrayOf(ByteArray(3), "000000"),
            arrayOf(ByteArray(4), "00000000"),
            arrayOf(ByteArray(5), "0000000000"),
            arrayOf(ByteArray(6), "000000000000"),
            arrayOf(ByteArray(36), "000000000000000000000000000000000000000000000000000000000000000000000000")
    )


    @Test
    fun testEncoding() {
        for (it in cases)
            assertEquals(Hex.encode(it[0] as ByteArray).toUpperCase(), (it[1] as String).toUpperCase())
    }

    @Test
    fun testDecoding() {
        for (it in cases)
            try {
                assertArrayEquals(it[0] as ByteArray, Hex.decode(it[1] as String))
            } catch (e: Exception) {
                fail("${it[1]} decoded to ${it[0]} is failed")
                e.printStackTrace()
            }
    }
}