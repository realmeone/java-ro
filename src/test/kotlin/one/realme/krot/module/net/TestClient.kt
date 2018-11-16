package one.realme.krot.module.net

import com.google.common.base.Stopwatch
import one.realme.krot.common.primitive.Block
import one.realme.krot.genesis
import one.realme.krot.module.chain.BlockChain
import one.realme.krot.module.net.client.PeerClient
import java.util.concurrent.TimeUnit

fun main(argv: Array<String>) {
    val stopwatch = Stopwatch.createStarted()
    val chain = BlockChain(Block.genesis())
    val client = PeerClient(chain = chain)
    client.connect()
    client.close()
    stopwatch.stop()
    println("all are done in ${stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")
}