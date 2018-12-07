package one.realme.krot.common.net

import one.realme.krot.common.codec.Hex
import one.realme.krot.net.Protocol
import org.junit.jupiter.api.Test

class ProtocolTest {
    @Test
    fun testProto() {
        val handshakeMsg = Protocol.Message.newBuilder().apply {
            type = Protocol.MessageType.PING
            ping = Protocol.Ping.newBuilder().apply {
                nonce = 100000000
            }.build()
        }.build()
        println(Hex.encode(handshakeMsg.toByteArray()))
    }
}