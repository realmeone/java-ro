package one.realme.krot.common.net.romtp

import one.realme.krot.common.lang.toByteArray
import one.realme.krot.common.net.legacy.content.Ping
import one.realme.krot.common.net.legacy.content.Pong
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.random.Random

class PingPongTest {

    @Test
    fun testToByteArray() {
        val nonce = Random.nextLong()
        val pingBytes = Ping(nonce).toByteArray()
        val pongBytes = Pong(nonce).toByteArray()
        assertEquals(8, pingBytes.size)
        assertEquals(8, pongBytes.size)
    }

    @Test
    fun testFromByteArray() {
        val nonce = Random.nextLong()
        val ping = Ping(nonce.toByteArray())
        val pong = Pong(nonce.toByteArray())
        assertEquals(nonce, ping.nonce)
        assertEquals(nonce, pong.nonce)
    }
}