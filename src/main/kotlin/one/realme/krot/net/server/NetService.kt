package one.realme.krot.net.server

import com.google.common.util.concurrent.AbstractExecutionThreadService
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.timeout.ReadTimeoutHandler
import one.realme.krot.net.message.MessageDecoder
import one.realme.krot.net.message.MessageEncoder
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 * NetService Service
 *
 * Remote Peer will connect to this NetService and exchange payload
 */
object NetService : AbstractExecutionThreadService() {
    private val log = LoggerFactory.getLogger(NetService.javaClass)

    private var port = 50505
    private var maxPeer = 256

    private lateinit var bossGroup: NioEventLoopGroup
    private lateinit var workerGroup: NioEventLoopGroup

    override fun startUp() {
        bossGroup = NioEventLoopGroup()
        workerGroup = NioEventLoopGroup()
    }

    override fun triggerShutdown() {
        workerGroup.shutdownGracefully().await()
        bossGroup.shutdownGracefully().await()
        log.info("NetService stopped.")
    }

    override fun run() {
        val server = ServerBootstrap()

        server.group(bossGroup, workerGroup)
        server.channel(NioServerSocketChannel::class.java)
        server.option(ChannelOption.SO_BACKLOG, maxPeer)
        server.handler(LoggingHandler())

        server.childHandler(object : ChannelInitializer<SocketChannel>() {
            override fun initChannel(ch: SocketChannel) {
                ch.pipeline().addLast(ReadTimeoutHandler(60, TimeUnit.SECONDS))
                ch.pipeline().addLast(MessageEncoder())
                ch.pipeline().addLast(MessageDecoder())
                ch.pipeline().addLast(MessageHandler())
            }
        })
        server.childOption(ChannelOption.SO_KEEPALIVE, true)
        server.bind(port).sync().addListener {
            if (it.isSuccess)
                log.info("NetService started on port: $port")
        }.channel().closeFuture().sync().addListener {
            if (it.isSuccess)
                log.info("NetService stopped")
        }
    }
}