package one.realme.krot.net.message

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import one.realme.krot.crypto.toInt

class MessageDecoder : ByteToMessageDecoder() {

    override fun decode(ctx: ChannelHandlerContext, `in`: ByteBuf, out: MutableList<Any>) {
        val data = ByteArray(`in`.readableBytes())
        `in`.readBytes(data)
        
        val cmd = data.copyOfRange(0, 4)
        val len = data.copyOfRange(4, 8)
        val cs = data.copyOfRange(8, 12)
        val pl = data.copyOfRange(12, data.size)
        cmd.reverse()
        len.reverse()
        cs.reverse()
        pl.reverse()

        val command = Command(cmd.toInt())
        val length = len.toInt()
        val checksum = cs.toInt()

        out.add(Message(command, pl, length, checksum))
    }

}