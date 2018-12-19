package one.realme.krot.service.net

import com.google.protobuf.ByteString
import io.netty.channel.Channel
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.net.Protocol
import one.realme.krot.service.chain.ChainService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.random.Random

/**
 * (Server) Peer - Peer (Client)
 *
 * features:
 * 1. broadcast tx & blx
 * 2. handle protocol message
 * 3. sync blx
 */
internal class Peer(
        val localNodeId: String,
        var remoteNodeId: String = "",
        val channel: Channel,
        val remoteIp: String = channel.remoteAddress().toString(),
        val cs: ChainService,
        val os: String,
        val agent: String,
        var handshakeCount: Int = 0
) {
    private val log: Logger = LoggerFactory.getLogger(Peer::class.java)

    var connected: Boolean = false
        get() = channel.isActive

    fun handleHandShake(msg: Protocol.Message) {
        // get handshake from remote peer
        // create peer if no this peer
        // check if remote peer height is higher
        //     start sync
        val remoteHandshake = msg.handShake
        sendHandshake()
    }

    fun handlePing(msg: Protocol.Message) {
        if (true) {
//                if (!handshakeCount.containsKey(ctx.channel().id().asLongText())) {
            sendDisconnect(Protocol.Disconnect.Reason.NO_HANDSHAKE)
            close()
        } else sendPong()
    }

    fun handlePong() {

    }

    fun handleFetchData(msg: Protocol.Message) {
        val skip = msg.fetchData.skip
        val limit = if (msg.fetchData.limit > 500) 500 else msg.fetchData.limit

        when (msg.fetchData.type) {
            Protocol.DataType.BLK -> {
                sendBlocks(skip, limit)
            }
            Protocol.DataType.TRX -> {
                sendTrxs(skip, limit)
            }
            else -> {
                // wrong type ,todo return error msg or just disconnect
            }
        }
    }

    fun sendHandshake() {
        val handshake = Protocol.Message.newBuilder().apply {
            version = 0x01
            type = Protocol.Message.Type.HANDSHAKE
            handShake = Protocol.HandShake.newBuilder()
                    .setTimestamp(UnixTime.now().toInt())
                    .setNodeId(ByteString.copyFrom(localNodeId.toByteArray()))
                    .setHeight(cs.getHeight())
                    .setOs(os)
                    .setAgent(agent)
                    .build()
        }.build()
        log.info("sending handshake message to $remoteIp ...")
        write(handshake)
    }

    fun sendTrxs(skip: Long, limit: Long) {
        val trxs = mutableListOf<Protocol.Tx>()
        cs.fetchTransactions(skip, limit).forEach {
        }

        val data = Protocol.Message.newBuilder().apply {
            version = 0x01
            type = Protocol.Message.Type.DATA
            data = Protocol.Data.newBuilder()
                    .setType(Protocol.DataType.TRX)
                    .addAllTxs(trxs)
                    .build()
        }.build()
        log.info("sending data message (trx from $skip to ${skip + limit}) to $remoteIp ...")
        write(data)
    }

    fun sendBlocks(skip: Long, limit: Long) {
        val blocks = mutableListOf<Protocol.Block>()
        cs.fetchBlocks(skip, limit).forEach {
        }

        val data = Protocol.Message.newBuilder().apply {
            version = 0x01
            type = Protocol.Message.Type.DATA
            data = Protocol.Data.newBuilder()
                    .setType(Protocol.DataType.BLK)
                    .addAllBlocks(blocks)
                    .build()
        }.build()
        log.info("sending data message (block from $skip to ${skip + limit}) to $remoteIp ...")
        write(data)
    }

    fun sendPing() {
        val ping = Protocol.Message.newBuilder().apply {
            version = 0x01
            type = Protocol.Message.Type.PING
            ping = Protocol.Ping.newBuilder().setNonce(Random.nextLong()).build()
        }.build()
        log.info("sending ping message to $remoteIp ...")
        write(ping)
    }

    fun sendPong() {
        val pong = Protocol.Message.newBuilder().apply {
            version = 0x01
            type = Protocol.Message.Type.PONG
            pong = Protocol.Pong.newBuilder().setNonce(Random.nextLong()).build()
        }.build()
        log.info("sending pong message to $remoteIp ...")
        write(pong)
    }

    fun sendDisconnect(rs: Protocol.Disconnect.Reason) {
        val goAway = Protocol.Message.newBuilder().apply {
            version = 0x01
            type = Protocol.Message.Type.DISCONNECT
            disconnect = Protocol.Disconnect.newBuilder().apply {
                reason = rs
                nodeId = ByteString.copyFrom(nodeId.toByteArray())
            }.build()
        }.build()
        write(goAway)
        close()
    }

    private fun write(msg: Protocol.Message) {
        channel.writeAndFlush(msg)
    }

    private fun close() {
        if (channel.isOpen) channel.close()
    }
}