package one.realme.krot.net

import com.google.common.base.Stopwatch
import one.realme.krot.net.client.PeerClient
import java.util.concurrent.TimeUnit

fun main(argv: Array<String>) {
    val stopwatch = Stopwatch.createStarted()
    val client = PeerClient()
    client.connect()
    client.close()
    stopwatch.stop()
    println("all are done in ${stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")
}