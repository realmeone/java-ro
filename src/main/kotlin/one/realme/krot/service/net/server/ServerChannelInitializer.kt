package one.realme.krot.service.net.server

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler
import one.realme.krot.service.chain.ChainService
import one.realme.krot.service.net.codec.MessageDecoder
import one.realme.krot.service.net.codec.MessageEncoder
import java.util.concurrent.TimeUnit

internal class ServerChannelInitializer(
        private val chainService: ChainService,
        private val conf: NetService.Configuration
) : ChannelInitializer<SocketChannel>() {

    override fun initChannel(ch: SocketChannel) {
        with(ch.pipeline()) {
            addLast(ReadTimeoutHandler(60, TimeUnit.SECONDS))
            addLast(MessageEncoder())
            addLast(MessageDecoder())
            addLast(ServerHandler(chainService, conf))
        }
    }

}