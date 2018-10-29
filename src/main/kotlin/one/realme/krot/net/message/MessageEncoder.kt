package one.realme.krot.net.message

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import one.realme.krot.common.toBytesLE

class MessageEncoder : MessageToByteEncoder<Message>() {
    override fun encode(ctx: ChannelHandlerContext, msg: Message, out: ByteBuf) {
        out.writeBytes(msg.command.code.toBytesLE() +
                msg.length.toBytesLE() +
                msg.checksum.reversedArray() +
                msg.payload.reversedArray())
    }
}