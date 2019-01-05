package one.realme.krot.service.net

import com.google.protobuf.ByteString
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.net.Protocol
import one.realme.krot.service.chain.ChainService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.random.Random

/**
 * (Server) Peer - Peer (Client)
 *
 * handle protocol message
 */
internal class Peer(
        val channel: Channel,
        val localNodeId: String,
        val remoteIp: String,
        val cs: ChainService,
        var remoteNodeId: String = "",
        var os: String = "unknown",
        var agent: String = "unknown",
        var handshakeCount: Int = 0
) {
    private val log: Logger = LoggerFactory.getLogger(Peer::class.java)
    private val protocolVersion = 0x01

    var connected: Boolean = false
        get() = channel.isActive
    var handshaked: Boolean = false
        get() = lastRecvHandshake != null && lastSendHandshake != null
    @Volatile
    var lastRecvHandshake: Protocol.Message? = null
    @Volatile
    var lastSendHandshake: Protocol.Message? = null

    // dispatch message
    fun handleMessage(msg: Protocol.Message) {
        // check if handshaked ? disconnect : continue
        if (msg.type == Protocol.Message.Type.HANDSHAKE) handleHandShake(msg)
        else if (!handshaked) {
            discardWithNoHandShake()
            close()
        } else
            when (msg.type) {
                Protocol.Message.Type.PING -> handlePing(msg)
                Protocol.Message.Type.PONG -> handlePong(msg)
                Protocol.Message.Type.FETCH_DATA -> handleFetchData(msg)
                Protocol.Message.Type.NOTICE_DATA -> handleNoticeData(msg)
                else -> close()
            }
    }

    private fun handleHandShake(msg: Protocol.Message) {
        lastRecvHandshake = msg
        val handshake = msg.handShake
        this.remoteNodeId = handshake.nodeId.toStringUtf8()

        // validate handshake
        var valid = true
        if (msg.handShake.nodeId.isEmpty) {
            log.info("Handshake message validation: NodeId is null string")
            valid = false
        }
        if (msg.handShake.os.isEmpty()) {
            log.info("Handshake message validation: OS is null string")
            valid = false
        }

        if (!valid) {
            log.info("Handshake message invalid")
            discardWithNoReason()
            return
        }

        sendHandshake()

        // todo check if remote peer height is higher
        //     tell sync manager we need catch up to the head
    }

    private fun handlePing(msg: Protocol.Message) {
        // check nonce
        sendPong()
    }

    private fun handlePong(msg: Protocol.Message) {
        // check nonce
    }

    private fun handleFetchData(msg: Protocol.Message) {
        val skip = msg.fetchData.skip
        val limit = if (msg.fetchData.limit > 500) 500 else msg.fetchData.limit

        when (msg.fetchData.type) {
            Protocol.DataType.BLK -> {
                sendBlocks(skip, limit)
            }
            Protocol.DataType.TRX -> {
                sendTrxs(skip, limit)
            }
            else -> discardWithBadData()
        }
    }

    private fun handleNoticeData(msg: Protocol.Message) {
        val data = msg.noticeData
        data.dataList.forEach {
            when (it.type) {
                Protocol.DataType.BLK -> {
                    // Todo receive blk data
                }
                Protocol.DataType.TRX -> {
                    // Todo receive transaction data
                }
                else -> discardWithBadData()
            }
        }
    }

    fun sendHandshake() {
        val handshake = Protocol.Message.newBuilder().apply {
            version = protocolVersion
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
        lastSendHandshake = handshake
        handshakeCount++
    }

    /**
     * send notice data from miner service
     */
    fun sendNoticeData() {
    }

    fun sendTrxs(skip: Long, limit: Long) {
        val trxs = mutableListOf<Protocol.Tx>()
        cs.fetchTransactions(skip, limit).forEach {
        }

        val data = Protocol.Message.newBuilder().apply {
            version = protocolVersion
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
            version = protocolVersion
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
            version = protocolVersion
            type = Protocol.Message.Type.PING
            ping = Protocol.Ping.newBuilder().setNonce(Random.nextLong()).build()
        }.build()
        log.info("sending ping message to $remoteIp ...")
        write(ping)
    }

    fun sendPong() {
        val pong = Protocol.Message.newBuilder().apply {
            version = protocolVersion
            type = Protocol.Message.Type.PONG
            pong = Protocol.Pong.newBuilder().setNonce(Random.nextLong()).build()
        }.build()
        log.info("sending pong message to $remoteIp ...")
        write(pong)
    }

    fun sendDisconnect(rs: Protocol.Disconnect.Reason) {
        val goAway = Protocol.Message.newBuilder().apply {
            version = protocolVersion
            type = Protocol.Message.Type.DISCONNECT
            disconnect = Protocol.Disconnect.newBuilder().apply {
                reason = rs
                nodeId = ByteString.copyFrom(nodeId.toByteArray())
            }.build()
        }.build()
        write(goAway).addListener(ChannelFutureListener.CLOSE)
        close()
    }

    fun discardWithTimeout() = sendDisconnect(Protocol.Disconnect.Reason.TIMEOUT)
    fun discardWithUnknownError() = sendDisconnect(Protocol.Disconnect.Reason.UNKNOWN_ERROR)
    fun discardWithNoReason() = sendDisconnect(Protocol.Disconnect.Reason.NO_REASON)
    fun discardWithNoHandShake() = sendDisconnect(Protocol.Disconnect.Reason.NO_HANDSHAKE)
    fun discardWithBadData() = sendDisconnect(Protocol.Disconnect.Reason.BAD_DATA)

    private fun write(msg: Protocol.Message): ChannelFuture = channel.writeAndFlush(msg)

    private fun close() {
        if (channel.isOpen) channel.close()
    }


}