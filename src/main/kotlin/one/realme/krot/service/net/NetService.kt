package one.realme.krot.service.net

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.timeout.ReadTimeoutHandler
import one.realme.krot.common.appbase.AbstractService
import one.realme.krot.program.Krot
import one.realme.krot.service.chain.ChainService
import one.realme.krot.service.net.romtp.MessageDecoder
import one.realme.krot.service.net.romtp.MessageEncoder
import one.realme.krot.service.net.server.PeerServer
import one.realme.krot.service.net.server.ServerHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class NetService(val chainService: ChainService) : AbstractService() {
    private val log: Logger = LoggerFactory.getLogger(PeerServer::class.java)
    private val bossGroup = NioEventLoopGroup()
    private val workerGroup = NioEventLoopGroup()
    private val peerServer = PeerServer(chainService)

    private var port: Int = 50505
    private var maxPeer: Int = 500

    override fun name(): String = "NetService"

    override fun initialize() {
        port = Krot.config.net.port
        maxPeer = Krot.config.net.maxPeer
    }

    @Synchronized
    override fun shutdown() {
        workerGroup.shutdownGracefully().await()
        bossGroup.shutdownGracefully().await()
        log.info("PeerService stopped.")
    }

    @Synchronized
    override fun startup() {
        thread(name = name()) {
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
}