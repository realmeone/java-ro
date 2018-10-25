package one.realme.krot.net.client

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import org.slf4j.LoggerFactory
import java.time.Instant

/**
 * A Peer
 *
 * Exchange data from another client normally
 */
class Peer(
        val host: String = "127.0.0.1",
        val port: Int = 50505
) {
    private val log = LoggerFactory.getLogger(Peer::class.java)
    private val workerGroup: EventLoopGroup

    init {
        workerGroup = NioEventLoopGroup()
    }

    fun connect() {
        log.info("connect to $host on port $port")
        // hand shake
        // share meta data
        // sync new blocks
        val b = Bootstrap()
        b.group(workerGroup)
                .channel(NioSocketChannel::class.java)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(object : ChannelInitializer<SocketChannel>() {
                    override fun initChannel(ch: SocketChannel) {
                        ch.pipeline().addLast(
                                // todo custom this
                                object : ChannelInboundHandlerAdapter() {
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

    fun close() {
        workerGroup.shutdownGracefully()
        workerGroup.terminationFuture().syncUninterruptibly()
    }
}