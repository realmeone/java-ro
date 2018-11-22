package one.realme.krot.common.support

import io.netty.bootstrap.ServerBootstrap
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
import java.util.concurrent.TimeUnit

class ChainService : AbstractService() {
    override fun initialize() {
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
    override fun initialize() {
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

    override fun initialize() {
        println("[${Thread.currentThread().name}] init ${name()}")
    }

    override fun start() {
        println("[${Thread.currentThread().name}] start ${name()}")
        startServerAsync()
        println("[${Thread.currentThread().name}] started ${name()}")
    }

    private fun startServerAsync() {
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
                .bind(50500)
        println("[${Thread.currentThread().name}] NetService listen on port: 50500")
    }

    override fun stop() {
        println("network shuting down now...")
        workerGroup.shutdownGracefully().await()
        bossGroup.shutdownGracefully().await()
        println("[${Thread.currentThread().name}] shutdown net service")

    }
}

object Node : Lifecycle {
    @Volatile
    private var running = false

    override fun isRunning(): Boolean = running

    private val serviceManager = ServiceManager()
    private val shutdownHook = ShutdownHook()

    fun init() {
        serviceManager.registerService(ChainService(), NetService(), RpcService())
        serviceManager.initialize()

        Runtime.getRuntime().addShutdownHook(shutdownHook)
    }

    class ShutdownHook : Thread() {
        override fun run() {
            Node.stop()
        }
    }

    override fun start() {
        if (running) return
        serviceManager.start()
        running = true
        println("[${Thread.currentThread().name}] application started.")
    }

    override fun stop() {
        if (!running) return
        Runtime.getRuntime().removeShutdownHook(shutdownHook) // avoid twice
        serviceManager.stop()
        running = false
        println("[${Thread.currentThread().name}] application stopped.")
    }
}

fun main(args: Array<String>) = runBlocking {
    Node.init()
    Node.start()
    delay(2000)
    Node.stop()
}