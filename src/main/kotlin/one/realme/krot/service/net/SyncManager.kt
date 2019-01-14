package one.realme.krot.service.net

import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.protobuf.ProtobufDecoder
import io.netty.handler.codec.protobuf.ProtobufEncoder
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender
import io.netty.handler.timeout.ReadTimeoutHandler
import one.realme.krot.net.Protocol
import one.realme.krot.service.chain.ChainService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

/**
 * hold active peers
 *
 * features
 *
 * 1. catch up to the head block
 * 2. broadcast tx or blk we received to the other peers
 * 3. send mined block to the other peers
 * 4. sort peers every 1 minutes
 *
 */
internal class SyncManager(
        private val seedPeers: MutableList<String>,
        private val localNodeId: String,
        private val os: String,
        private val agent: String,
        private val chain: ChainService
) {
    private val log: Logger = LoggerFactory.getLogger(SyncManager::class.java)
    private val peers = mutableMapOf<String, Peer>()

    internal fun getPeer(uniqueName: String): Peer? = peers[uniqueName]
    internal fun addPeer(uniqueName: String, peer: Peer) = peers.putIfAbsent(uniqueName, peer)

    fun startSync() {
        seedPeers.forEach {
            connect(it)
        }
    }

    private fun connect(peerAddr: String) {
        if (peers.values.find { it.peerAddr == peerAddr } != null) {
            // already active
            return
        }
        val colon = peerAddr.indexOf(":")

        if (colon <= 0) {
            log.warn("Invalid peerAddr address. must be \"host:port\": $peerAddr")
        }
        val host = peerAddr.substring(0, colon)
        val port = peerAddr.substring(colon + 1)

        val peer = Peer(
                createChannel(host, port.toInt()),
                localNodeId,
                peerAddr,
                chain,
                "",
                os,
                agent
        )
        peer.sendHandshake()
        addPeer(peerAddr, peer)
    }

    private fun createChannel(host: String, port: Int): Channel =
            Bootstrap().group(NioEventLoopGroup())
                    .channel(NioSocketChannel::class.java)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(object : ChannelInitializer<NioSocketChannel>() {
                        override fun initChannel(ch: NioSocketChannel) {
                            with(ch.pipeline()) {
                                addLast(ReadTimeoutHandler(60, TimeUnit.SECONDS))
                                addLast(ProtobufVarint32FrameDecoder())
                                addLast(ProtobufDecoder(Protocol.Message.getDefaultInstance()))
                                addLast(ProtobufVarint32LengthFieldPrepender())
                                addLast(ProtobufEncoder())
                                addLast(ClientHandler())
                            }
                        }
                    })
                    .connect(host, port).sync()
                    .channel()


    fun stopSync() {

    }
}