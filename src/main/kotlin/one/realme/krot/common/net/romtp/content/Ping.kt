package one.realme.krot.common.net.romtp.content

import one.realme.krot.common.lang.toByteArray
import kotlin.random.Random

data class Ping(
        val nonce: Long = Random.nextLong()//  8 bytes, random number
) {
    fun toByteArray(): ByteArray = nonce.toByteArray()
}