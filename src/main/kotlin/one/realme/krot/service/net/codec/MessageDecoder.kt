package one.realme.krot.service.net.codec

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import one.realme.krot.common.lang.toInt
import one.realme.krot.common.net.romtp.Message
import one.realme.krot.common.net.romtp.MessageType

internal class MessageDecoder : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        val data = ByteArray(`in`.readableBytes())
        `in`.readBytes(data)
        out.add(Message.fromByteArray(data))
    }

}