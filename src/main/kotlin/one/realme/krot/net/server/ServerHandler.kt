package one.realme.krot.net.server

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.ReadTimeoutException
import one.realme.krot.net.romtp.Message
import one.realme.krot.net.romtp.header.MessageType
import org.slf4j.LoggerFactory

class ServerHandler : SimpleChannelInboundHandler<Message>() {
    private val log = LoggerFactory.getLogger(ServerHandler::class.java)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        log.info("received from ${ctx.channel().remoteAddress()} : [$msg]")
        when (msg.type) {
            MessageType.PING -> {
                ctx.writeAndFlush(Message.PONG)
            }
            MessageType.GET_TIME -> {
                ctx.writeAndFlush(Message.time())
            }
            MessageType.DISCONNECT -> {
                ctx.close()
            }
            MessageType.INV -> {
//                ctx.writeAndFlush(Message.inv())
            }
            else -> ctx.close()
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        log.info("Peer Client ${ctx.channel().remoteAddress()} is connected.")
        ctx.writeAndFlush(Message.HELLO)
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