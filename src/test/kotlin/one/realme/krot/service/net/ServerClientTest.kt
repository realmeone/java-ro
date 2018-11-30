package one.realme.krot.service.net

import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.util.concurrent.RejectedExecutionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

data class Request(val ctx: ChannelHandlerContext, val msg: String)

object Server {

    @JvmStatic
    fun main(args: Array<String>) {
        val p = Runtime.getRuntime().availableProcessors()
        val connectionGroup = NioEventLoopGroup(1)
        val workerGroup = NioEventLoopGroup(100)
        val b = ServerBootstrap().apply {
            group(connectionGroup, workerGroup)
            channel(NioServerSocketChannel::class.java)
            option(ChannelOption.SO_BACKLOG, 500)
            childOption(ChannelOption.SO_KEEPALIVE, true)
            childOption(ChannelOption.TCP_NODELAY, true)
            childHandler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(ch: SocketChannel) {
                    with(ch.pipeline()) {
                        addLast(ReadTimeoutHandler(60, TimeUnit.SECONDS))
                        addLast(object : ChannelInboundHandlerAdapter() {
                            override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                                msg as ByteBuf
                                // decode message
                                val msgByteArray = ByteArray(msg.readableBytes())
                                msg.readBytes(msgByteArray)
                                val message = String(msgByteArray)

                                // some heavy works
                                Thread.sleep(5000)
                                println("${Thread.currentThread()} read message ${message}")
                            }

                            override fun channelReadComplete(ctx: ChannelHandlerContext) {
                                ctx.close()
                            }
                        })
                    }
                }
            })
        }
        b.bind(8300).sync().channel().closeFuture().sync()
    }
}

object Client {

    @JvmStatic
    fun main(argv: Array<String>) = runBlocking {
        for (i in 1..100) {
            async {
                Worker().run()
            }.join()
        }
        println("client write done")
//        val stopwatch = Stopwatch.createStarted()
//        val chain = BlockChain(Block.genesis())
//        val client = PeerClient(chain = chain)
//        client.connect()
//        client.close()
//        stopwatch.stop()
//        println("all are done in ${stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")
    }

    class Worker {

        fun run() {
            val workerGroup: EventLoopGroup = NioEventLoopGroup()
            val channel = Bootstrap().apply {
                group(workerGroup)
                channel(NioSocketChannel::class.java)
                option(ChannelOption.SO_KEEPALIVE, true)
                handler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(ReadTimeoutHandler(60, TimeUnit.SECONDS))
                        ch.pipeline().addLast(object : SimpleChannelInboundHandler<String>() {
                            override fun channelRead0(ctx: ChannelHandlerContext, msg: String) {
                                // no handle from server just close
                                ctx.close()
                            }
                        })
                    }
                })
            }.connect("127.0.0.1", 8300).sync().channel()
            val message = "sometext".toByteArray()
            if (channel.isActive) {
                channel.writeAndFlush(Unpooled.buffer(message.size).writeBytes(message))
                channel.close()
            }
            workerGroup.shutdownGracefully()
        }
    }
}