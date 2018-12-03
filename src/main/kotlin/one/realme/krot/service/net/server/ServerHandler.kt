package one.realme.krot.service.net.server

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.ReadTimeoutException
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.common.net.romtp.Message
import one.realme.krot.common.net.romtp.MessageType.Companion.GET_TIME
import one.realme.krot.common.net.romtp.MessageType.Companion.HANDSHAKE
import one.realme.krot.common.net.romtp.MessageType.Companion.PING
import one.realme.krot.common.net.romtp.content.HandShake
import one.realme.krot.common.net.romtp.content.NetAddr
import one.realme.krot.common.primitive.Hash
import one.realme.krot.service.chain.ChainService
import org.slf4j.LoggerFactory

internal class ServerHandler(
        private val chain: ChainService,
        private val conf: NetService.Configuration
) : SimpleChannelInboundHandler<Message>() {
    private val log = LoggerFactory.getLogger(ServerHandler::class.java)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        log.info("received from ${ctx.channel().remoteAddress()} : [$msg]")
        // messages from peer client
        when (msg.type) {
            HANDSHAKE -> {
                val handshake = Message(
                        type = HANDSHAKE,
                        content = HandShake(0x01,
                                UnixTime.now().toInt(),
                                conf.nodeId,
                                NetAddr(conf.ip, conf.port),
                                chain.getHeight(),
                                conf.os,
                                conf.agent).toByteArray())
                ctx.writeAndFlush(handshake)
            }
            PING -> {
                ctx.writeAndFlush(Message.pong())
            }
            GET_TIME -> {
                ctx.writeAndFlush(Message.time())
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