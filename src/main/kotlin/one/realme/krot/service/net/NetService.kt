package one.realme.krot.service.net

import com.google.common.util.concurrent.AbstractExecutionThreadService
import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.util.NetUtil
import one.realme.krot.program.Krot
import one.realme.krot.service.chain.ChainService
import one.realme.krot.service.net.romtp.MessageDecoder
import one.realme.krot.service.net.romtp.MessageEncoder
import one.realme.krot.service.net.server.ServerHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class NetService(val chainService: ChainService) : AbstractExecutionThreadService() {
    private val log: Logger = LoggerFactory.getLogger(NetService::class.java)
    private val bossGroup = NioEventLoopGroup()
    private val workerGroup = NioEventLoopGroup()

    private var port: Int = 50505
    private var maxPeer: Int = 500

    override fun startUp() {
        port = Krot.config.net.port
        maxPeer = Krot.config.net.maxPeer
    }

    override fun run() {
        val b = createServerBootstrap()
        b.group(bossGroup, workerGroup)
        b.channel(NioServerSocketChannel::class.java)
        b.childHandler(object : ChannelInitializer<SocketChannel>() {
            override fun initChannel(ch: SocketChannel) {
                ch.pipeline().addLast(ReadTimeoutHandler(60, TimeUnit.SECONDS))
                ch.pipeline().addLast(MessageEncoder())
                ch.pipeline().addLast(MessageDecoder())
                ch.pipeline().addLast(ServerHandler(chainService))
            }
        })
        b.bind()
    }

    private fun createServerBootstrap(): ServerBootstrap = ServerBootstrap()
            .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .option(ChannelOption.SO_REUSEADDR, true)
            .option(ChannelOption.SO_BACKLOG, maxPeer)
            .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .childOption(ChannelOption.SO_RCVBUF, 1024 * 1024)
            .childOption(ChannelOption.SO_SNDBUF, 1024 * 1024)
            .childOption(ChannelOption.AUTO_READ, false)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
            .localAddress(port)


    override fun triggerShutdown() {
        workerGroup.shutdownGracefully().await()
        bossGroup.shutdownGracefully().await()
        log.info("PeerService stopped.")
    }

}