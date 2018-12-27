package one.realme.krot.service.net

import com.google.protobuf.ByteString
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import one.realme.krot.common.base.Application
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.net.Protocol
import one.realme.krot.service.chain.ChainService

object ProtoServer {
    val netService = NetService()

    @JvmStatic
    fun main(args: Array<String>) {
        val app = Application(servs = listOf(ChainService(), netService))
        app.start()
    }
}

object ProtoClient {

    @JvmStatic
    fun main(argv: Array<String>) = runBlocking {
        for (i in 1..5) {
            async {
                Worker().run()
            }.join()
        }
    }

    class Worker {

        suspend fun run() {
            val client = PeerClient(peerManager = ProtoServer.netService.peerManager)
            client.connect()
            val handshake = Protocol.Message.newBuilder().apply {
                version = 0x01
                type = Protocol.Message.Type.HANDSHAKE
                handShake = Protocol.HandShake.newBuilder()
                        .setTimestamp(UnixTime.now().toInt())
                        .setNodeId(ByteString.copyFrom(ProtoServer.netService.configuration.nodeId.toByteArray()))
                        .setHeight(5)
                        .setOs(ProtoServer.netService.configuration.os)
                        .setAgent(ProtoServer.netService.configuration.agent)
                        .build()
            }.build()
            client.write(handshake)
//
//            val ping = Protocol.Message.newBuilder().apply {
//                version = 0x01
//                type = Protocol.Message.Type.PING
//                ping = Protocol.Ping.newBuilder().apply {
//                    nonce = Random.nextLong()
//                }.build()
//            }.build()
//            client.write(ping)
//
//            val fetchData = Protocol.Message.newBuilder().apply {
//                version = 0x01
//                type = Protocol.Message.Type.FETCH_DATA
//                fetchData = Protocol.FetchData.newBuilder()
//                        .setSkip(0)
//                        .setLimit(10)
//                        .setType(Protocol.DataType.BLK).build()
//            }.build()
//            client.write(fetchData)

            delay(2000)
            client.close()
        }
    }
}