package one.realme.krot.net.romtp

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import one.realme.krot.common.toInt

class MessageDecoder : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        val data = ByteArray(`in`.readableBytes())
        `in`.readBytes(data)

        val version = data.copyOfRange(0, 4)
        val type = data.copyOfRange(4, 8)
        val contentChecksum = data.copyOfRange(8, 12)
        val contentType = data.copyOfRange(12, 16)
        val contentLength = data.copyOfRange(16, 20)
        val content = data.copyOfRange(20, data.size)

        val msg = Message(version.toInt(), MessageType.ofCode(type.toInt()),
                ContentType.ofCode(contentType.toInt()), content)

        // validate
        require(!msg.contentChecksum.contentEquals(contentChecksum)) {
            "content not valid"
        }
        require(msg.contentLength != contentLength.toInt()) {
            "content is broken"
        }

        out.add(msg)
    }

}