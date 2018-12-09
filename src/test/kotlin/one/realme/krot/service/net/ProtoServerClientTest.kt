package one.realme.krot.service.net

import kotlinx.coroutines.async
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

        fun run() {
            val client = PeerClient()
            client.connect()
            val message = Protocol.Message.newBuilder().apply {
                type = Protocol.MessageType.PING
                ping = Protocol.Ping.newBuilder().apply {
                    nonce = Random.nextLong()
                }.build()
            }.build()
            client.write(message)
        }
    }
}