package one.realme.net

import com.google.common.base.Stopwatch
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

object ClientTest {
    private val workerGroup = NioEventLoopGroup()

    private fun send() {
        val b = Bootstrap()
        b.group(workerGroup)
                .channel(NioSocketChannel::class.java)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(object : ChannelInboundHandlerAdapter() {
                            override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                                val m: ByteBuf = msg as ByteBuf
                                try {
                                    val time = m.readLong()
                                    println(Instant.ofEpochSecond(time).toString())
                                    ctx.close()
                                } finally {
                                    m.release()
                                }
                            }

                            override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
                                cause.printStackTrace()
                                ctx.close()
                            }
                        })
                    }
                })

        b.connect("127.0.0.1", 50505).sync()
                .channel().closeFuture().sync()
    }

    @JvmStatic
    fun main(argv: Array<String>) {
        val executor = Executors.newCachedThreadPool()
        val futures = ArrayList<Future<*>>()

        val stopwatch = Stopwatch.createStarted()
        for (i in 1..5000) {
            futures.add(executor.submit {
                send()
            })
        }
        futures.parallelStream().forEach {
            it.get()
        }

        workerGroup.shutdownGracefully()
        stopwatch.stop()
        println("all are done in ${stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")
        executor.shutdown()
    }
}