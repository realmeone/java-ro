package one.realme.krot.common.net.romtp.content

import one.realme.krot.common.lang.toByteArray

class NetAddr(
        // 16 bytes 10 bytes zero, 2 bytes ff 4bytes ipv4, e.g. 000000000000
        // 00 00 00 00 00 00 00 00 00 00 FF FF + IP4
        private val ip: ByteArray,
        private val port: Int // 4 bytes
) {
    fun toByteArray(): ByteArray = ip + port.toByteArray()

}