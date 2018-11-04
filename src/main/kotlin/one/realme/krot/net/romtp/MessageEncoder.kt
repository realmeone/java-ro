package one.realme.krot.net.romtp

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import one.realme.krot.common.toBytes

class MessageEncoder : MessageToByteEncoder<Message>() {
    override fun encode(ctx: ChannelHandlerContext, msg: Message, out: ByteBuf) {
        out.writeBytes(msg.version.toBytes() +
                msg.type.toBytes() +
                msg.contentChecksum +
                msg.contentType.toBytes() +
                msg.contentLength.toBytes() +
                msg.content)
    }
}