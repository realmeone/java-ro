package one.realme.krot.net.server

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import one.realme.krot.common.UnixTime
import one.realme.krot.net.message.Message
import org.slf4j.LoggerFactory

class MessageHandler : SimpleChannelInboundHandler<Message>() {
    private val log = LoggerFactory.getLogger(MessageHandler::class.java)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {

    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        log.info("Peer ${ctx.channel().remoteAddress()} is disconnected.")
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        log.info("Peer ${ctx.channel().remoteAddress()} is connected.")
        val time = ctx.alloc().buffer(8)
        time.writeInt(UnixTime.now().toInt())
        val f = ctx.writeAndFlush(time)
        f.addListeners(ChannelFutureListener.CLOSE)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}