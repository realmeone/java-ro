package one.realme.krot.common.net.romtp.content

class NetAddr(
        // 16 bytes 10 bytes zero, 2 bytes ff 4bytes ipv4, e.g. 000000000000
        // 00 00 00 00 00 00 00 00 00 00 FF FF + IP4
        val ip: ByteArray,
        val port: Short // 2 bytes
) {

}