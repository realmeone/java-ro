package one.realme.krot.common.net.legacy.content

import one.realme.krot.common.primitive.Hash
import one.realme.krot.common.lang.toByteArray
import one.realme.krot.common.lang.toInt

class InventoryVector(
        val type: Int, // 4bytes
        val hash: Hash// 32 bytes
) {

    fun toByteArray(): ByteArray = type.toByteArray() + hash.toByteArray()

    companion object {
        const val ERROR = 0
        const val TRANSACTION = 1
        const val BLOCK = 2
        const val FILTERED_BLOCK = 3

        fun fromBytes(raw: ByteArray): InventoryVector {
            val type = raw.copyOfRange(0, 4)
            val hashRef = raw.copyOfRange(4, 36)
            return InventoryVector(
                    type.toInt(),
                    Hash.fromBytes(hashRef)
            )
        }
    }
}