package one.realme.krot.service.net

import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.protobuf.ProtobufDecoder
import io.netty.handler.codec.protobuf.ProtobufEncoder
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender
import io.netty.handler.timeout.ReadTimeoutHandler
import kotlinx.coroutines.awaitAll
import one.realme.krot.common.net.romtp.Message
import one.realme.krot.net.Protocol
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 * A Peer client
 */
class PeerClient(
        val host: String = "127.0.0.1",
        val port: Int = 50505
) {
    private val log = LoggerFactory.getLogger(PeerClient::class.java)
    private val workerGroup: EventLoopGroup = NioEventLoopGroup()
    private lateinit var channel: Channel

    fun connect() {
        channel = Bootstrap().group(workerGroup)
                .channel(NioSocketChannel::class.java)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(object : ChannelInitializer<NioSocketChannel>() {
                    override fun initChannel(ch: NioSocketChannel) {
                        with(ch.pipeline()) {
                            addLast(ReadTimeoutHandler(60, TimeUnit.SECONDS))
                            addLast(ProtobufVarint32FrameDecoder())
                            addLast(ProtobufDecoder(Protocol.Message.getDefaultInstance()))
                            addLast(ProtobufVarint32LengthFieldPrepender())
                            addLast(ProtobufEncoder())
                            addLast(ClientHandler())
                        }
                    }

                })
                .connect(host, port).sync()
                .channel()
    }

    fun write(msg: Protocol.Message) {
        if (channel.isActive) {
            channel.writeAndFlush(msg)
            log.info("write message to server ")
        }
    }

    fun close() {
        log.info("closing client.")
        // send leave message and close
//        channel.writeAndFlush(byebye)

        val closeFuture = if (channel.isOpen) channel.close() else null
        try {
            workerGroup.shutdownGracefully().await()
        } finally {
            closeFuture?.sync()
        }
        log.info("client closed")
    }
}