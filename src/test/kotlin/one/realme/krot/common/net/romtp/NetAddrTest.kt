package one.realme.krot.common.net.romtp

import one.realme.krot.common.codec.Hex
import one.realme.krot.common.codec.toHexString
import one.realme.krot.common.net.romtp.content.NetAddr
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.InetAddress

class NetAddrTest {
    private val port = 7001

    @Test
    fun testToByteArrayIpv6() {
        val rawIpv61 = "1080:0:0:0:8:800:200C:417A"
        val ipv6 = InetAddress.getByName(rawIpv61)
        val na = NetAddr(ipv6.address, port)

        assertFalse(na.isIpv4)
        assertEquals(rawIpv61, na.ip)
        assertEquals(port, na.port)
    }

    @Test
    fun testFromByteArrayIpv6() {
        val rawIpv61 = "1080:0:0:0:8:800:200C:417A"
        val rawIpv6Hex = "108000000000000000080800200c417a00001b59"
        val bytes = Hex.decode(rawIpv6Hex)
        val na = NetAddr(bytes)
        assertFalse(na.isIpv4)
        assertEquals(rawIpv61, na.ip)
        assertEquals(port, na.port)
    }

    @Test
    fun testToByteArray() {
        val rawIpv4 = "192.168.0.1"
        val addr = InetAddress.getByName(rawIpv4).address
        val na = NetAddr(addr, port)
        val bytes = na.toByteArray()
        var offset = 0
        bytes.forEachIndexed { i, b ->
            if (i < 10)
                assertEquals(0, b)
            if (i in 10..11)
                assertEquals(0xff.toByte(), b)
            if (i in 12..15)
                assertEquals(addr[offset++], b)
        }
        assertEquals(rawIpv4, na.ip)
        assertTrue(na.isIpv4)
        assertEquals(port, na.port)
    }

    @Test
    fun testFromByteArray() {
        val bytes = Hex.decode("00000000000000000000ffffa9fead2100001b59")
        val na = NetAddr(bytes)
        assertEquals(InetAddress.getLocalHost().hostAddress, na.ip)
        assertTrue(na.isIpv4)
        assertEquals(port, na.port)
    }
}