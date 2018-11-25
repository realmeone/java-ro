package one.realme.krot.service.net.server

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler
import one.realme.krot.service.chain.ChainService
import one.realme.krot.service.net.romtp.MessageDecoder
import one.realme.krot.service.net.romtp.MessageEncoder
import java.util.concurrent.TimeUnit

class ServerChannelInitializer(
        private val chainService: ChainService
) : ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        with(ch.pipeline()) {
            addLast(ReadTimeoutHandler(60, TimeUnit.SECONDS))
            addLast(MessageEncoder())
            addLast(MessageDecoder())
            addLast(ServerHandler(chainService))
        }
    }

}