package one.realme.krot.common.net.legacy.content

import one.realme.krot.common.lang.toByteArray
import one.realme.krot.common.lang.toInt
import java.net.InetAddress

/**
 * 20 bytes(16 bytes + 4 bytes)
 *
 * 16 bytes 10 bytes zero, 2 bytes ff 4bytes ipv4,
 * e.g. 00 00 00 00 00 00 00 00 00 00 FF FF + IPV4
 */
class NetAddr {
    private val data: ByteArray = ByteArray(20)
    val isIpv4: Boolean
    val port: Int
    val ip: String

    constructor(
            ip: ByteArray,
            port: Int // 4 bytes
    ) {
        this.isIpv4 = ip.size == 4
        this.port = port

        if (isIpv4) { // ipv4
            data[10] = 0xff.toByte()
            data[11] = 0xff.toByte()
            ip.forEachIndexed { i, b ->
                data[12 + i] = b
            }
            this.ip = InetAddress.getByAddress(data.copyOfRange(12, 16)).hostAddress
        } else {
            ip.forEachIndexed { i, b ->
                data[i] = b
            }
            this.ip = InetAddress.getByAddress(data.copyOfRange(0, 16)).hostAddress.toUpperCase()
        }
        port.toByteArray().forEachIndexed { i, b ->
            data[16 + i] = b
        }
    }

    constructor(raw: ByteArray) {
        raw.copyInto(data)
        isIpv4 = ByteArray(10).contentEquals(data.copyOf(10)) &&
                data[10] == 0xff.toByte() &&
                data[11] == 0xff.toByte()
        if (isIpv4) this.ip = InetAddress.getByAddress(data.copyOfRange(12, 16)).hostAddress
        else this.ip = InetAddress.getByAddress(data.copyOfRange(0, 16)).hostAddress.toUpperCase()
        port = data.copyOfRange(16, 20).toInt()
    }

    fun toByteArray(): ByteArray = data.clone()
}