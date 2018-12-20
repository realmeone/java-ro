package one.realme.krot.common.net.legacy.content

import one.realme.krot.common.lang.toByteArray
import one.realme.krot.common.lang.toLong
import kotlin.random.Random

/**
 * 8 bytes, random number
 */
class Ping {
    val nonce: Long

    constructor(nonce: Long = Random.nextLong()) {
        this.nonce = nonce
    }

    constructor(raw: ByteArray) {
        nonce = raw.toLong()
    }

    fun toByteArray(): ByteArray = nonce.toByteArray()
}