package one.realme.krot

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import one.realme.krot.common.lang.measureTimeSeconds
import org.junit.jupiter.api.Test
import kotlin.concurrent.thread

class CoroutineChannelTest {
    data class Tx(val content: String)
    internal object App {
        val txChannel = Channel<Tx>()
    }

    internal class NetService {
        fun onMessageReceive() {
            GlobalScope.launch {
                App.txChannel.send(Tx("some text"))
            }
        }
    }

    internal class ChainService {
        fun start() {
            runBlocking {
                for (tx in App.txChannel) {
                    launch {
                        //                        println("receive tx: $tx")
                    }
                }
            }
        }
    }

    @Test
    fun testCoroutinesChannel() {
        val net = NetService()
        val chain = ChainService()
        thread {
            chain.start()
        }
        val sc = measureTimeSeconds {
            repeat(100000) {
                net.onMessageReceive()
            }
        }
        println("all message done in $sc seconds")
    }
}