package one.realme.krot.service.net

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.ReadTimeoutException
import one.realme.krot.net.Protocol.Message
import one.realme.krot.net.Protocol.Message.Type.*
import one.realme.krot.service.chain.ChainService
import org.slf4j.LoggerFactory

internal class ServerHandler(
        private val chain: ChainService,
        private val conf: NetService.Configuration
) : SimpleChannelInboundHandler<Message>() {
    private val log = LoggerFactory.getLogger(ServerHandler::class.java)
    private val peers = mutableMapOf<String, Peer>()

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        log.info("received from ${ctx.channel().remoteAddress()} : [$msg]")
        // messages from peer client
        var peer = peers[ctx.channel().id().asShortText()]
        if (null == peer) {
            peer = Peer(
                    localNodeId = conf.nodeId.toString(),
                    remoteNodeId = "",
                    channel = ctx.channel(),
                    cs = chain,
                    os = conf.os,
                    agent = conf.agent
            )
            peers[ctx.channel().id().asShortText()] = peer
        }

        when (msg.type) {
            HANDSHAKE -> peer.handleHandShake(msg)
            PING -> peer.handlePing(msg)
            FETCH_DATA -> peer.handleFetchData(msg)
            else -> ctx.close()
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        log.info("Peer ${ctx.channel().remoteAddress()} is connected.")
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        log.info("Peer ${ctx.channel().remoteAddress()} is disconnected.")
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