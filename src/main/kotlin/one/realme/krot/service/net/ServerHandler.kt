package one.realme.krot.service.net

import com.google.protobuf.ByteString
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.ReadTimeoutException
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.net.Protocol
import one.realme.krot.service.chain.ChainService
import org.slf4j.LoggerFactory
import kotlin.random.Random

internal class ServerHandler(
        private val chain: ChainService,
        private val conf: NetService.Configuration
) : SimpleChannelInboundHandler<Protocol.Message>() {
    private val log = LoggerFactory.getLogger(ServerHandler::class.java)
    private val peers = mutableMapOf<String, Peer>()

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Protocol.Message) {
        log.info("received from ${ctx.channel().remoteAddress()} : [$msg]")
        // messages from peer client

        when (msg.type) {
            Protocol.MessageType.HANDSHAKE -> {
                // get handshake from remote peer
                // create peer if no this peer
                // check if remote peer height is higher
                //     start sync

                val remoteHandshake = msg.handShake

                val handshake = Protocol.Message.newBuilder().apply {
                    version = 0x01
                    type = Protocol.MessageType.HANDSHAKE
                    handShake = Protocol.HandShake.newBuilder().apply {
                        timestamp = UnixTime.now().toInt()
                        nodeId = ByteString.copyFrom(conf.nodeId.toByteArray())
                        height = chain.getHeight()
                        os = conf.os
                        agent = conf.agent
                    }.build()
                }
                ctx.writeAndFlush(handshake)
            }
            Protocol.MessageType.PING -> {
                if (true) {
//                if (!handshakeCount.containsKey(ctx.channel().id().asLongText())) {
                    val goAway = Protocol.Message.newBuilder().apply {
                        version = 0x01
                        type = Protocol.MessageType.DISCONNECT
                        disconnect = Protocol.Disconnect.newBuilder().apply {
                            reason = Protocol.Reason.NO_HANDSHAKE
                            nodeId = ByteString.copyFrom(conf.nodeId.toByteArray())
                        }.build()
                    }
                    ctx.writeAndFlush(goAway)
                    ctx.close()
                } else {
                    log.info("receive ping from ${ctx.channel().remoteAddress()}")
                    val pong = Protocol.Message.newBuilder().apply {
                        type = Protocol.MessageType.PONG
                        pong = Protocol.Pong.newBuilder().setNonce(Random.nextLong()).build()
                    }.build()
                    ctx.writeAndFlush(pong)
                }
            }
            Protocol.MessageType.GET_DATA -> {

            }
            else -> ctx.close()
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        log.info("Peer Client ${ctx.channel().remoteAddress()} is connected.")
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        log.info("Peer Client ${ctx.channel().remoteAddress()} is disconnected.")
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        // if timeout send timeout
        when (cause) {
            is ReadTimeoutException -> {
                val result = "out of time".toByteArray()
                val byteBuf = ctx.alloc().buffer(result.size).writeBytes(result)
                ctx.writeAndFlush(byteBuf).addListener(ChannelFutureListener.CLOSE)
                log.info("Peer ${ctx.channel().remoteAddress()} is timeout.")
            }
            else -> {
                val result = "unknown error".toByteArray()
                val byteBuf = ctx.alloc().buffer(result.size).writeBytes(result)
                ctx.writeAndFlush(byteBuf).addListener(ChannelFutureListener.CLOSE)
                log.info("an unknown error out from ${ctx.channel().remoteAddress()}", cause)
            }
        }
        ctx.close()
    }
}