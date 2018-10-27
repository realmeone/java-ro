package one.realme.krot.net.server

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.ReadTimeoutException
import one.realme.krot.common.UnixTime
import one.realme.krot.net.message.Message
import org.slf4j.LoggerFactory

class MessageHandler : SimpleChannelInboundHandler<Message>() {
    private val log = LoggerFactory.getLogger(MessageHandler::class.java)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        val cmd = String(msg.payload)
        log.info("received from ${ctx.channel().remoteAddress()} : [$cmd]")
        when (cmd) {
            "ping" -> {
                val pong = "pong".toByteArray()
                ctx.writeAndFlush(Message(pong))
            }
            "time" -> {
                val time = ctx.alloc().buffer(8)
                time.writeInt(UnixTime.now().toInt())
                ctx.writeAndFlush(Message(time.array()))
            }
            else -> ctx.close()
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        log.info("Peer ${ctx.channel().remoteAddress()} is connected.")
        ctx.writeAndFlush(Message("hello".toByteArray()))
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        log.info("Peer ${ctx.channel().remoteAddress()} is disconnected.")
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
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