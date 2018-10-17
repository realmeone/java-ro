package one.realme.chain

import one.realme.common.UnixTime
import one.realme.common.Version
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BlockTest {

    @Test
    fun testBlock() {
        val b1 = Block(Version.CURRENT, 1, Hash.empty(), UnixTime.now())
        val b2 = Block(Version.CURRENT, 2, b1.hash(), UnixTime.now())
        assertEquals(b1.hash(), b2.header().prevBlockHash)
        println(b1.hash())
        println(b2.hash())
    }
}