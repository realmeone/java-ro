package one.realme.krot.common.base

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.timeout.ReadTimeoutHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ChainService : AbstractService() {
    override fun initialize(app: Application) {
        println("[${Thread.currentThread().name}] init ${name()}")
    }

    override fun start() {
        println("[${Thread.currentThread().name}] start ${name()}")
    }

    override fun stop() {
        println("[${Thread.currentThread().name}] shutdown ${name()}")
    }
}

class RpcService : AbstractService() {
    override fun initialize(app: Application) {
        println("[${Thread.currentThread().name}] init ${name()}")
    }

    override fun start() {
        println("[${Thread.currentThread().name}] start ${name()}")
    }

    override fun stop() {
        println("[${Thread.currentThread().name}] shutdown ${name()}")
    }

}

class NetService : AbstractService() {
    private val bossGroup = NioEventLoopGroup(1)
    private val workerGroup = NioEventLoopGroup()

    override fun initialize(app: Application) {
        println("[${Thread.currentThread().name}] init ${name()}")
    }

    override fun start() {
        println("[${Thread.currentThread().name}] start ${name()}")
        startServerAsync()
        println("[${Thread.currentThread().name}] started ${name()}")
    }

    private fun startServerAsync() {
        val b = createServerBootstrap()
        b.group(bossGroup, workerGroup)
        b.channel(NioServerSocketChannel::class.java)
        b.handler(LoggingHandler())
        b.childHandler(object : ChannelInitializer<SocketChannel>() {
            override fun initChannel(ch: SocketChannel) {
                ch.pipeline().addLast(ReadTimeoutHandler(60, TimeUnit.SECONDS))
                ch.pipeline().addLast(object : ChannelInboundHandlerAdapter() {
                    // do nothing
                })
            }
        })
        b.bind()
        println("[${Thread.currentThread().name}] NetService listen on port: 50500")
    }

    private fun createServerBootstrap() = ServerBootstrap()
            .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .option(ChannelOption.SO_REUSEADDR, true)
            .option(ChannelOption.SO_BACKLOG, 500)
            .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
            .childOption(ChannelOption.SO_RCVBUF, 1024 * 1024)
            .childOption(ChannelOption.SO_SNDBUF, 1024 * 1024)
            .childOption(ChannelOption.AUTO_READ, false)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
            .localAddress(50050)

    override fun stop() {
        println("network shuting down now...")
        workerGroup.shutdownGracefully().await()
        bossGroup.shutdownGracefully().await()
        println("[${Thread.currentThread().name}] shutdown net service")

    }
}


fun main(args: Array<String>) {
    val app = Application(ChainService(), NetService(), RpcService())
    app.start()
    val osVersion = System.getProperty("os.name")
    if (osVersion.contains("Win")) {
        Thread.sleep(5000)
        app.stop()
    }
}