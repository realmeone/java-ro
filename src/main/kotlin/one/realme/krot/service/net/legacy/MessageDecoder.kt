package one.realme.krot.service.net.legacy

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import one.realme.krot.common.net.legacy.Message

internal class MessageDecoder : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        val data = ByteArray(`in`.readableBytes())
        `in`.readBytes(data)
        out.add(Message.fromByteArray(data))
    }

}