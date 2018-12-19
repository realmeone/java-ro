package one.realme.krot.service.net

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.protobuf.ProtobufDecoder
import io.netty.handler.codec.protobuf.ProtobufEncoder
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender
import io.netty.handler.timeout.ReadTimeoutHandler
import one.realme.krot.common.base.Application
import one.realme.krot.common.base.BaseService
import one.realme.krot.common.primitive.Hash
import one.realme.krot.net.Protocol
import one.realme.krot.service.chain.ChainService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.util.concurrent.TimeUnit

/**
 * netty based
 */
class NetService : BaseService() {

    internal class Configuration {
        private val parallelism: Int = Runtime.getRuntime().availableProcessors()
        var connectionGroupSize = parallelism / 2 + 1
        var workerGroupSize = parallelism / 2 + 1
        var port = 50505
        var maxPeer = 500
        val nodeId = Hash.random()
        val ip: String = InetAddress.getLocalHost().hostAddress
        val os: String = System.getProperty("os.name")
        val agent: String = "krot"
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
                getIntOrNull("net.port")?.let { port = it }
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
            option(ChannelOption.SO_BACKLOG, configuration.maxPeer)
            childOption(ChannelOption.SO_KEEPALIVE, true)
            childOption(ChannelOption.TCP_NODELAY, true)
            localAddress(configuration.port)
            group(connectionGroup, workerGroup)
            channel(NioServerSocketChannel::class.java)
            childHandler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(ch: SocketChannel) {
                    with(ch.pipeline()) {
                        addLast(ReadTimeoutHandler(60, TimeUnit.SECONDS))
                        addLast(ProtobufVarint32FrameDecoder())
                        addLast(ProtobufDecoder(Protocol.Message.getDefaultInstance()))
                        addLast(ProtobufVarint32LengthFieldPrepender())
                        addLast(ProtobufEncoder())
                        addLast(ServerHandler(chainService, configuration))
                    }
                }
            })
        }
    }

    override fun start() {
        channel = serverBootstrap.bind().sync().channel()
        log.info("NodeId: ${configuration.nodeId}")
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