package one.realme.chain

import one.realme.common.Version
import one.realme.common.toUnix
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class BlockTest {

    @Test
    fun testBlock() {
        val b1 = Block(Version.CURRENT, 2, Hash.empty(), Date().toUnix())
        val b2 = Block(Version.CURRENT, 2, b1.hash(), Date().toUnix())
        assertEquals(b1.hash(), b2.header().prevBlockHash)
    }
}