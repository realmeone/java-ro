package one.realme.krot.service.net

import com.google.protobuf.ByteString
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
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import one.realme.krot.common.base.Application
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.net.Protocol
import one.realme.krot.service.chain.ChainService
import java.util.concurrent.TimeUnit

object ProtoServerClientTest {
    val netService = NetService()
    val chainService = ChainService()
    @JvmStatic
    fun main(argv: Array<String>) = runBlocking {
        async {
            val app = Application(servs = listOf(chainService, netService))
            app.start()
        }
        for (i in 1..1) {
            async {
                Worker().run()
            }.join()
            close()
        }
    }

    val nelg = NioEventLoopGroup()
    fun close() {
        nelg.shutdownGracefully().await()
    }
    
    class Worker {
        suspend fun run() {
            val channel = createChannel("127.0.0.1", 50505, nelg)
            val handshake = Protocol.Message.newBuilder().apply {
                version = 0x01
                type = Protocol.Message.Type.HANDSHAKE
                handShake = Protocol.HandShake.newBuilder()
                        .setTimestamp(UnixTime.now().toInt())
                        .setNodeId(ByteString.copyFrom(netService.configuration.nodeId.toByteArray()))
                        .setHeight(5)
                        .setOs(netService.configuration.os)
                        .setAgent(netService.configuration.agent)
                        .build()
            }.build()
            channel.writeAndFlush(handshake)
//
//            val ping = Protocol.Message.newBuilder().apply {
//                version = 0x01
//                type = Protocol.Message.Type.PING
//                ping = Protocol.Ping.newBuilder().apply {
//                    nonce = Random.nextLong()
//                }.build()
//            }.build()
//            client.write(ping)
//
//            val fetchData = Protocol.Message.newBuilder().apply {
//                version = 0x01
//                type = Protocol.Message.Type.FETCH_DATA
//                fetchData = Protocol.FetchData.newBuilder()
//                        .setSkip(0)
//                        .setLimit(10)
//                        .setType(Protocol.DataType.BLK).build()
//            }.build()
//            client.write(fetchData)
            delay(2000)
            channel.close().sync().await()
//            channel.eventLoop().shutdownGracefully().await()
        }

        private fun createChannel(host: String, port: Int, nelg: NioEventLoopGroup): Channel =
                Bootstrap().group(nelg)
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
                                    addLast(ServerHandler(chainService, netService.syncManager, netService.configuration.nodeId.toString()))
                                }
                            }
                        })
                        .connect(host, port).sync()
                        .channel()
    }
}