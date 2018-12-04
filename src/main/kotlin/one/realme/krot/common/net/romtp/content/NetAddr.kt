package one.realme.krot.common.net.romtp.content

import one.realme.krot.common.lang.toByteArray
import one.realme.krot.common.lang.toInt

/**
 * 20 bytes(16 bytes + 4 bytes)
 *
 * 16 bytes 10 bytes zero, 2 bytes ff 4bytes ipv4, e.g. 000000000000
 * 00 00 00 00 00 00 00 00 00 00 FF FF + IPV4
 */
class NetAddr {
    private val data: ByteArray = ByteArray(20)
    val isIpv4: Boolean
    val port: Int

    // TODO support ipv6
    val ip: String by lazy {
        data.copyOfRange(12, 16).joinToString(".") {
            (it.toInt() and 0xff).toString()
        }
    }

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
        } else {
            ip.forEachIndexed { i, b ->
                data[i] = b
            }
        }
        port.toByteArray().forEachIndexed { i, b ->
            data[16 + i] = b
        }
    }

    constructor(raw: ByteArray) {
        raw.copyInto(data)
        isIpv4 = data[10] == 0xff.toByte() && data[11] == 0xff.toByte()
        port = data.copyOfRange(16, 20).toInt()
    }

    fun toByteArray(): ByteArray = data.clone()
}