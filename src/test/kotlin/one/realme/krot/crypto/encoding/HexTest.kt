package one.realme.krot.crypto.encoding

import one.realme.krot.crypto.encoding.Hex
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.util.stream.IntStream
import kotlin.system.measureTimeMillis

/**
 * test cases from https://tools.ietf.org/html/rfc4648#page-10
 * see Page 12. Test Vectors Base16
 *
 * why not use guava BaseEncoding.base16 to do Hex thing?
 * guava base16 cannot pass the test cases "testDecoding",
 * it require UpperCase or LowerCase, so we implements one
 */
class HexTest {
    @Test
    fun whoIsFasterDecode() {
        val raw = "666f6f"

        val round = 5000001
        println("Hex decode round : $round")

        val myTimeUsed = measureTimeMillis {
            IntStream.range(0, round).parallel().forEach {
                Hex.decode(raw)
            }
        }
        println("my use time : ${myTimeUsed / 1000.0} seconds")

        val bcTimeUsed = measureTimeMillis {
            IntStream.range(0, round).parallel().forEach {
                org.bouncycastle.util.encoders.Hex.decode(raw)
            }
        }
        println("BC use time : ${bcTimeUsed / 1000.0} seconds")
    }

    @Test
    fun whoIsFasterEncode() {
        val raw = "foo".toByteArray()

        val round = 500000
        println("Hex round : $round")
        val myTimeUsed = measureTimeMillis {
            IntStream.range(0, round).parallel().forEach {
                Hex.encode(raw)
            }
        }
        println("my use time : ${myTimeUsed / 1000.0} seconds")

        val bcTimeUsed = measureTimeMillis {
            IntStream.range(0, round).parallel().forEach {
                org.bouncycastle.util.encoders.Hex.encode(raw)
            }
        }
        println("BC use time : ${bcTimeUsed / 1000.0} seconds")
    }

    private val cases = listOf(
            listOf("".toByteArray(), ""),
            listOf("1".toByteArray(), "31"),
            listOf("A".toByteArray(), "41"),
            listOf("f".toByteArray(), "66"),
            listOf("fo".toByteArray(), "666f"),
            listOf("foo".toByteArray(), "666f6f"),
            listOf("foob".toByteArray(), "666f6f62"),
            listOf("fooba".toByteArray(), "666f6f6261"),
            listOf("foobar".toByteArray(), "666f6f626172"),
            listOf("Hello World".toByteArray(), "48656C6C6F20576F726C64"),
            listOf(ByteArray(1) { 10 }, "0a"),
            listOf(ByteArray(1) { 255.toByte() }, "ff"),
            listOf(ByteArray(1), "00"),
            listOf(ByteArray(2), "0000"),
            listOf(ByteArray(3), "000000"),
            listOf(ByteArray(4), "00000000"),
            listOf(ByteArray(5), "0000000000"),
            listOf(ByteArray(6), "000000000000"),
            listOf(ByteArray(36), "000000000000000000000000000000000000000000000000000000000000000000000000")
    )


    @Test
    fun testHexEncode() {
        for (it in cases)
            assertEquals(Hex.encode(it[0] as ByteArray).toUpperCase(), (it[1] as String).toUpperCase())
    }

    @Test
    fun testHexDecode() {
        for (it in cases)
            try {
                assertArrayEquals(it[0] as ByteArray, Hex.decode(it[1] as String))
            } catch (e: Exception) {
                fail("${it[1]} decoded to ${it[0]} is failed", e)
            }
    }
}