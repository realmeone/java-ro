package one.realme.krot.service.net.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.Channel
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import one.realme.krot.common.base.Application
import one.realme.krot.common.base.BaseService
import one.realme.krot.service.chain.ChainService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * netty based
 */
class NetService : BaseService() {

    internal class Configuration {
        private val parallelism: Int = Runtime.getRuntime().availableProcessors()
        var connectionGroupSize = parallelism / 2 + 1
        var workerGroupSize = parallelism / 2 + 1
        var port: Int = 50505
        var maxPeer: Int = 500
        var connectTimeoutMillis: Int = 30000
    }

    private val log: Logger = LoggerFactory.getLogger(NetService::class.java)
    private val configuration = Configuration()

    private lateinit var chainService: ChainService
    private lateinit var connectionGroup: NioEventLoopGroup
    private lateinit var workerGroup: NioEventLoopGroup
    private lateinit var serverBootstrap: ServerBootstrap
    private lateinit var channel: Channel

    override fun initialize(app: Application) {
        // set config
        with(configuration) {
            with(app.config) {
                getIntOrNull("net.prot")?.let { port = it }
                getIntOrNull("net.maxPeer")?.let { maxPeer = it }
            }
        }

        chainService = app.services[ChainService::class.java.simpleName] as ChainService

        requireNotNull(chainService) {
            "must init chain service first"
        }

        connectionGroup = NioEventLoopGroup(configuration.connectionGroupSize)
        workerGroup = NioEventLoopGroup(configuration.workerGroupSize)

        serverBootstrap = ServerBootstrap().apply {
            option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            option(ChannelOption.SO_REUSEADDR, true)
            option(ChannelOption.SO_BACKLOG, configuration.maxPeer)
            childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            childOption(ChannelOption.SO_RCVBUF, 1024 * 1024)
            childOption(ChannelOption.SO_SNDBUF, 1024 * 1024)
            childOption(ChannelOption.AUTO_READ, false)
            childOption(ChannelOption.SO_KEEPALIVE, true)
            childOption(ChannelOption.TCP_NODELAY, true)
            childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, configuration.connectTimeoutMillis)
            localAddress(configuration.port)
            group(connectionGroup, workerGroup)
            channel(NioServerSocketChannel::class.java)
            childHandler(ServerChannelInitializer(chainService))
        }
    }

    override fun start() {
        channel = serverBootstrap.bind().sync().channel()
        log.info("${name()} listen on port: ${configuration.port}")
    }


    override fun stop() {
        val closeFuture = if (channel.isOpen) channel.close() else null
        try {
            workerGroup.shutdownGracefully().await()
            connectionGroup.shutdownGracefully().await()
        } finally {
            closeFuture?.sync()
        }

        log.info("${name()} stopped.")
    }

}