package one.realme.krot.common.net

import io.netty.channel.Channel
import java.net.InetSocketAddress

fun Channel.peerAddr(): String {
    val isa = remoteAddress() as InetSocketAddress
    return "${isa.hostString}:${isa.port}"
}