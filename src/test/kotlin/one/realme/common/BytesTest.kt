package one.realme.common

import one.realme.crypto.encoding.Hex
import org.junit.Test


class BytesTest {

    @Test
    fun testIntToBytes() {
        println(Hex.encode(Int.MIN_VALUE.toBytes()))
        println(Hex.encode(Int.MAX_VALUE.toBytes()))
    }
}