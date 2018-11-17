package one.realme.krot.service.net.server

import com.google.common.util.concurrent.AbstractExecutionThreadService
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.timeout.ReadTimeoutHandler
import one.realme.krot.program.Context
import one.realme.krot.service.net.romtp.MessageDecoder
import one.realme.krot.service.net.romtp.MessageEncoder
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 * PeerService (facade)
 *
 * dependency to chain service
 */
class PeerService : AbstractExecutionThreadService() {
    private val log = LoggerFactory.getLogger(PeerService::class.java)

    private val chainService = Context.chainService
    private val port = Context.config.net.port
    private val maxPeer = Context.config.net.maxPeer

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
                        ch.pipeline().addLast(ServerHandler(chainService))
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