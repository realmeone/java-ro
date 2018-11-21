package one.realme.krot.common.appbase

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.timeout.ReadTimeoutHandler
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

class ChainService : AbstractService() {
    override fun name(): String = "chainService"

    override fun initialize() {
        println("[${Thread.currentThread().name}] init chain service")
    }

    override fun startup() {
        println("[${Thread.currentThread().name}] start chain service")
    }

    override fun shutdown() {
        println("[${Thread.currentThread().name}] shutdown chain service")
    }
}

class NetService : AbstractService() {
    private val bossGroup = NioEventLoopGroup()
    private val workerGroup = NioEventLoopGroup()

    override fun name(): String = "NetService"

    override fun initialize() {
        println("[${Thread.currentThread().name}] init net service")
    }

    override fun startup() {
        println("[${Thread.currentThread().name}] start net service")
        ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .option(ChannelOption.SO_BACKLOG, 500)
                .handler(LoggingHandler())
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(ReadTimeoutHandler(60, TimeUnit.SECONDS))
                        ch.pipeline().addLast(object : ChannelInboundHandlerAdapter() {
                            // do nothing
                        })
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .bind(50500).sync().addListener {
                    if (it.isSuccess)
                        println("[${Thread.currentThread().name}] NetService listen on port: 50500")
                }.channel().closeFuture().sync().addListener {
                    if (it.isSuccess)
                        println("[${Thread.currentThread().name}] NetService port closed")
                }
    }

    override fun shutdown() {
        workerGroup.shutdownGracefully().awaitUninterruptibly()
        bossGroup.shutdownGracefully().awaitUninterruptibly()
        println("[${Thread.currentThread().name}] shutdown net service")
    }
}

fun main(args: Array<String>) = runBlocking {
    val serviceManager = ServiceManager()
    serviceManager.registerService(ChainService())
    serviceManager.registerService(NetService())

    Runtime.getRuntime().addShutdownHook(Thread {
        println("[${Thread.currentThread().name}] application stopped.")
        serviceManager.shutdown()
    })

    serviceManager.initialize()
    serviceManager.startup()
}