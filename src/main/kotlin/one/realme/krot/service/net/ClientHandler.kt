package one.realme.krot.service.net


import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.ReadTimeoutException
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.common.net.romtp.Message
import one.realme.krot.common.net.romtp.MessageType
import one.realme.krot.net.Protocol
import org.slf4j.LoggerFactory

class ClientHandler : SimpleChannelInboundHandler<Protocol.Message>() {
    private val log = LoggerFactory.getLogger(ClientHandler::class.java)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Protocol.Message) {
        log.info("received from ${ctx.channel().remoteAddress()} : [$msg]")
        when (msg.type) {
            Protocol.MessageType.HANDSHAKE -> {
            }
            Protocol.MessageType.PONG -> {
                log.info("receive from server pong.")
                ctx.disconnect()
                ctx.close()
            }
            Protocol.MessageType.TIME -> {

            }
            else -> ctx.close()
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        log.info("Peer Server ${ctx.channel().remoteAddress()} is connected.")
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        log.info("Peer Server ${ctx.channel().remoteAddress()} is disconnected.")
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        when (cause) {
            is ReadTimeoutException -> {
                log.info("Peer ${ctx.channel().remoteAddress()} is timeout.")
            }
        }
        ctx.close()
    }

}