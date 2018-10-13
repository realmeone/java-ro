package one.realme.net

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import one.realme.common.toUnix
import org.slf4j.LoggerFactory
import java.util.*

class ByteInHandler : ChannelInboundHandlerAdapter() {
    private val log = LoggerFactory.getLogger(ByteInHandler::class.java)

    override fun channelActive(ctx: ChannelHandlerContext) {
        log.info("Peer ${ctx.channel().remoteAddress()} is connected.")
        val time = ctx.alloc().buffer(8)
        time.writeLong(Date().toUnix())
        val f = ctx.writeAndFlush(time)
        f.addListeners(ChannelFutureListener.CLOSE)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        cause.printStackTrace()
        ctx.close()
    }
}