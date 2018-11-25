package one.realme.krot.service.net.client


import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.ReadTimeoutException
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.service.chain.BlockChain
import one.realme.krot.service.net.romtp.Message
import one.realme.krot.service.net.romtp.MessageType
import org.slf4j.LoggerFactory

class ClientHandler(val chain: BlockChain) : SimpleChannelInboundHandler<Message>() {
    private val log = LoggerFactory.getLogger(ClientHandler::class.java)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        log.info("received from ${ctx.channel().remoteAddress()} : [$msg]")
        when (msg.type) {
            MessageType.VERSION -> {
            }
            MessageType.PONG -> {
            }
            MessageType.TIME -> {
                log.info(UnixTime.fromBytes(msg.content).toString())
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