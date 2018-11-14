package one.realme.krot.net.romtp.content

import one.realme.krot.common.toByteArray
import kotlin.random.Random

data class Pong(
        val nonce: Long = Random.nextLong() // 8 bytes
) {
    fun toByteArray(): ByteArray = nonce.toByteArray()
}