package one.realme.krot.module.net.romtp

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import one.realme.krot.common.lang.toByteArray

class MessageEncoder : MessageToByteEncoder<Message>() {
    override fun encode(ctx: ChannelHandlerContext, msg: Message, out: ByteBuf) {
        if (0 == msg.length)
            out.writeBytes(msg.version.toByteArray() + msg.type.toByteArray())
        else
            out.writeBytes(msg.version.toByteArray() +
                    msg.type.toByteArray() +
                    msg.length.toByteArray() +
                    msg.content)
    }
}