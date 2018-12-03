package one.realme.krot

import com.typesafe.config.ConfigFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.net.InetAddress
import kotlin.experimental.and


class IdeaSet {
    @ParameterizedTest
    @CsvSource(
            "mainnet.conf, mainnet, 50505",
            "testnet.conf, testnet, 50505"
    )
    fun testTypeSafeConfig(file: String, eName: String, ePort: Int) {
        val env = ConfigFactory.load(file)
        assertEquals(eName, env.getString("name"))
        assertEquals(ePort, env.getInt("net.port"))
    }

    @Test
    fun testNetAddr() {
        val a1 = getIpString(InetAddress.getLocalHost().address)
        val a2 = InetAddress.getLocalHost().hostAddress
        assertEquals(a1, a2)
    }

    private fun getIpString(address: ByteArray): String =
            with(StringBuilder()) {
                address.forEachIndexed { i, byte ->
                    append(byte.toInt() and 0xff)
                    if (i < 3)
                        append('.')
                }
                toString()
            }

    @Test
    fun testOS() {
        println(System.getProperty("os.name"))
    }

}