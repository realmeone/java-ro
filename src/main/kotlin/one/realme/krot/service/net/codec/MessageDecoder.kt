package one.realme.krot.service.net.codec

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import one.realme.krot.common.lang.toInt
import one.realme.krot.common.net.romtp.Message
import one.realme.krot.common.net.romtp.MessageType

class MessageDecoder : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        val data = ByteArray(`in`.readableBytes())
        `in`.readBytes(data)

        val version = data.copyOfRange(0, 4)
        val type = data.copyOfRange(4, 8)
        val contentLength = data.copyOfRange(8, 12)
        if (0 == contentLength.toInt())
            out.add(Message(version.toInt(), MessageType.ofCode(type.toInt())))
        else {
            val content = data.copyOfRange(12, data.size)
            out.add(Message(version.toInt(), MessageType.ofCode(type.toInt()), content))
        }
    }

}