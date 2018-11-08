package one.realme.krot

import com.typesafe.config.ConfigFactory
import org.bouncycastle.util.encoders.Hex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater


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
    fun testZlib() {
        val bytes = "herehere".toByteArray()
        println("origin: ${Hex.toHexString(bytes)}")
        println("zlib compressed: ${Hex.toHexString(bytes.zlibCompress())}")
    }

    private fun ByteArray.zlibDecompress(): ByteArray {
        val data = this
        val inflater = Inflater()
        inflater.setInput(data)
        return ByteArrayOutputStream(data.size).use {
            val buffer = ByteArray(1024)
            while (!inflater.finished()) {
                val count = inflater.inflate(buffer)
                it.write(buffer, 0, count)
            }
            inflater.end()
            it.toByteArray()
        }
    }

    private fun ByteArray.zlibCompress(): ByteArray {
        val data = this
        val deflater = Deflater()
        deflater.setInput(data)
        return ByteArrayOutputStream(data.size).use {
            val buffer = ByteArray(1024)
            while (!deflater.finished()) {
                val count = deflater.deflate(buffer) // returns the generated code... index
                it.write(buffer, 0, count)
            }
            deflater.end()
            it.toByteArray()
        }
    }
}