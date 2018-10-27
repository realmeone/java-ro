package one.realme.krot.net.message

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

class MessageDecoder : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        println("decode bytes[$`in`] to message")
        val data = ByteArray(`in`.readableBytes())
        `in`.readBytes(data)
        out.add(Message(data))
    }

}