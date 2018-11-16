package one.realme.krot.module.net.server

import com.google.common.util.concurrent.AbstractExecutionThreadService
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.timeout.ReadTimeoutHandler
import one.realme.krot.program.AppContext
import one.realme.krot.module.net.romtp.MessageDecoder
import one.realme.krot.module.net.romtp.MessageEncoder
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 * PeerService Service
 */
class PeerService(val context: AppContext) : AbstractExecutionThreadService() {
    private val log = LoggerFactory.getLogger(PeerService::class.java)

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
        log.info("PeerService stopped.")
    }

    override fun run() {
        ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .option(ChannelOption.SO_BACKLOG, maxPeer)
                .handler(LoggingHandler())
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(ReadTimeoutHandler(60, TimeUnit.SECONDS))
                        ch.pipeline().addLast(MessageEncoder())
                        ch.pipeline().addLast(MessageDecoder())
                        ch.pipeline().addLast(ServerHandler(context.chain))
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .bind(port).sync().addListener {
                    if (it.isSuccess)
                        log.info("PeerService started on port: $port")
                }.channel().closeFuture().sync().addListener {
                    if (it.isSuccess)
                        log.info("PeerService stopped")
                }
    }
}