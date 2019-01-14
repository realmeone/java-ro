package one.realme.krot.service.net

import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.ReadTimeoutException
import one.realme.krot.common.net.peerAddr
import one.realme.krot.net.Protocol.Message
import one.realme.krot.service.chain.ChainService
import org.slf4j.LoggerFactory

@ChannelHandler.Sharable
internal class ServerHandler(
        private val chain: ChainService,
        private val syncManager: SyncManager,
        private val nodeId: String
) : SimpleChannelInboundHandler<Message>() {
    private val log = LoggerFactory.getLogger(ServerHandler::class.java)

    override fun channelRead0(ctx: ChannelHandlerContext, msg: Message) {
        log.info("received from ${ctx.channel().peerAddr()} : [$msg]")
        // messages from peer client
        val peer = syncManager.getPeer(ctx.channel().peerAddr())
        if (null == peer) ctx.close() else peer.handleMessage(msg)
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        log.info("peer[${ctx.channel().id()}] ${ctx.channel().peerAddr()} is connected.")

        syncManager.addPeer(ctx.channel().peerAddr(), Peer(
                ctx.channel(),
                nodeId,
                ctx.channel().peerAddr(),
                chain
        ))
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        log.info("Peer ${ctx.channel().peerAddr()} is disconnected.")
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        val peer = syncManager.getPeer(ctx.channel().peerAddr())
        if (null == peer) ctx.close()
        else when (cause) {
            is ReadTimeoutException -> peer.discardWithTimeout()
            else -> peer.discardWithUnknownError()
        }
        // note: remove peer from syncManager ?
    }
}