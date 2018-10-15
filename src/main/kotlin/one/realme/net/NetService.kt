package one.realme.net

import com.google.common.util.concurrent.AbstractExecutionThreadService
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.slf4j.LoggerFactory

/**
 * NetService Service
 *
 * Remote Peer will connect to this NetService Service and exchange data
 *
 * use vert.x replaced later?
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
                .channel(NioServerSocketChannel::class.java)
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(ByteInHandler())
                    }
                })
                .option(ChannelOption.SO_BACKLOG, maxPeer)
                .childOption(ChannelOption.SO_KEEPALIVE, true)

        server.bind(port).sync().addListener {
            if (it.isSuccess)
                log.info("NetService started on port: $port")
        }.channel().closeFuture().sync()
    }
}