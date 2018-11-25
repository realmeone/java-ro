package one.realme.krot.service.net.client

import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler
import one.realme.krot.service.chain.BlockChain
import one.realme.krot.service.net.romtp.Message
import one.realme.krot.service.net.romtp.MessageDecoder
import one.realme.krot.service.net.romtp.MessageEncoder
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 * A Peer client
 */
class PeerClient(
        val host: String = "127.0.0.1",
        val port: Int = 50505,
        val chain: BlockChain
) {
    private val log = LoggerFactory.getLogger(PeerClient::class.java)
    private val workerGroup: EventLoopGroup = NioEventLoopGroup()
    private lateinit var channel: Channel

    fun connect() {
        val f = Bootstrap().group(workerGroup)
                .channel(NioSocketChannel::class.java)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(ReadTimeoutHandler(60, TimeUnit.SECONDS))
                        ch.pipeline().addLast(MessageEncoder())
                        ch.pipeline().addLast(MessageDecoder())
                        ch.pipeline().addLast(ClientHandler(chain))
                    }
                })
                .connect(host, port).sync()
                .channel().closeFuture().sync()
        channel = f.channel()
    }

    fun write(msg: Message) {
        if (channel.isActive)
            channel.writeAndFlush(msg)
    }

    fun close() {
        channel.close()
        workerGroup.shutdownGracefully()
    }
}