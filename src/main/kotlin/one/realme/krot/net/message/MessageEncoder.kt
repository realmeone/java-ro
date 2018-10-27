package one.realme.krot.net.message

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class MessageEncoder : MessageToByteEncoder<Message>() {
    override fun encode(ctx: ChannelHandlerContext, msg: Message, out: ByteBuf) {
        println("encode $msg to bytes")
        out.writeBytes(msg.payload)
    }
}