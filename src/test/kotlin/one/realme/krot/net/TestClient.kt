package one.realme.krot.net

import com.google.common.base.Stopwatch
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.logging.LoggingHandler
import one.realme.krot.net.message.Message
import one.realme.krot.net.message.MessageDecoder
import one.realme.krot.net.message.MessageEncoder
import one.realme.krot.net.server.NetService
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class In : SimpleChannelInboundHandler<Message>() {
    private val log = LoggerFactory.getLogger(In::class.java)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        log.info("read from msg $msg")
        when (msg.toString()) {
            "hello" -> {
                ctx.writeAndFlush(Message("ping".toByteArray()))
                        .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
            }
            "pong" -> {
                ctx.writeAndFlush(Message("time".toByteArray()))
                        .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE)
            }
            else -> {
                ctx.writeAndFlush(Message("exit".toByteArray()))
            }
        }

    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        log.info("connect to host: [${ctx.channel().remoteAddress()}]")
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        log.info("leave host : [${ctx.channel().remoteAddress()}")
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}

class TestClient {
    private val workerGroup = NioEventLoopGroup()
    private lateinit var channel: Channel

    fun connect() {
        val b = Bootstrap()
        b.group(workerGroup)
        b.channel(NioSocketChannel::class.java)
        b.option(ChannelOption.SO_KEEPALIVE, true)
        b.handler(object : ChannelInitializer<SocketChannel>() {
            override fun initChannel(ch: SocketChannel) {
                ch.pipeline().addLast(LoggingHandler())
                ch.pipeline().addLast(MessageEncoder())
                ch.pipeline().addLast(MessageDecoder())
                ch.pipeline().addLast(In())
            }
        })
        val f = b.connect("127.0.0.1", 50505).sync()
        channel = f.channel()
        channel.closeFuture().sync()
    }

    fun write(msg: Message) {
        if (channel.isActive)
            channel.writeAndFlush(msg)
    }

    fun close() {
        channel.close()
        workerGroup.shutdownGracefully()
    }
}

fun main(argv: Array<String>) {
    val stopwatch = Stopwatch.createStarted()
    val client = TestClient()
    client.connect()
    client.close()
    stopwatch.stop()
    println("all are done in ${stopwatch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")
}