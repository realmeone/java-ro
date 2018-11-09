package one.realme.krot

import com.typesafe.config.ConfigFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


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

}