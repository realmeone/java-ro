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
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class ChainService : AbstractService() {
    override fun initialize() {
        println("[${Thread.currentThread().name}] init ${name()}")
    }

    override fun startup() {
        println("[${Thread.currentThread().name}] start ${name()}")
    }

    override fun shutdown() {
        println("[${Thread.currentThread().name}] shutdown ${name()}")
    }
}

class RpcService : AbstractService() {
    override fun initialize() {
        println("[${Thread.currentThread().name}] init ${name()}")
    }

    override fun startup() {
        println("[${Thread.currentThread().name}] start ${name()}")
    }

    override fun shutdown() {
        println("[${Thread.currentThread().name}] shutdown ${name()}")
    }

}

class NetService : AbstractService() {
    private val bossGroup = NioEventLoopGroup()
    private val workerGroup = NioEventLoopGroup()

    override fun initialize() {
        println("[${Thread.currentThread().name}] init ${name()}")
    }

    override fun startup() {
        println("[${Thread.currentThread().name}] start ${name()}")
        startServerAsync()
        println("[${Thread.currentThread().name}] started ${name()}")
    }

    private fun startServerAsync() {
        thread {
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
    }

    override fun shutdown() {
        println("network shuting down now...")
        workerGroup.shutdownGracefully().await()
        bossGroup.shutdownGracefully().await()
        println("[${Thread.currentThread().name}] shutdown net service")

    }
}

class Node {
    private val serviceManager = ServiceManager()

    fun init() {
        serviceManager.registerService(ChainService(), NetService(), RpcService())
        serviceManager.initialize()

        Runtime.getRuntime().addShutdownHook(Thread {
            stop()
        })
    }

    fun start() {
        serviceManager.startup()
        println("[${Thread.currentThread().name}] application started.")
    }

    fun stop() {
        serviceManager.shutdown()
        println("[${Thread.currentThread().name}] application stopped.")
    }
}

fun main(args: Array<String>) {
    val node = Node()
    node.init()
    node.start()
}