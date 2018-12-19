package one.realme.krot.service.net

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import one.realme.krot.common.base.Application
import one.realme.krot.net.Protocol
import one.realme.krot.service.chain.ChainService
import kotlin.random.Random

object ProtoServer {

    @JvmStatic
    fun main(args: Array<String>) {
        val app = Application(servs = listOf(ChainService(), NetService()))
        app.start()
    }
}

object ProtoClient {

    @JvmStatic
    fun main(argv: Array<String>) = runBlocking {
        //        for (i in 1..100) {
        async {
            Worker().run()
        }.join()
//        }
    }

    class Worker {

        suspend fun run() {
            val client = PeerClient()
            client.connect()
            val ping = Protocol.Message.newBuilder().apply {
                version = 0x01
                type = Protocol.Message.Type.PING
                ping = Protocol.Ping.newBuilder().apply {
                    nonce = Random.nextLong()
                }.build()
            }.build()
            client.write(ping)

            val fetchData = Protocol.Message.newBuilder().apply {
                version = 0x01
                type = Protocol.Message.Type.FETCH_DATA
                fetchData = Protocol.FetchData.newBuilder()
                        .setSkip(0)
                        .setLimit(10)
                        .setType(Protocol.DataType.BLK).build()
            }.build()
            client.write(fetchData)

            delay(2000)
            client.close()
        }
    }
}