package one.realme.krot.common.net.legacy.content

import java.nio.ByteBuffer

/**
 * getdata is used in response to inv,
 * to retrieve the content of a specific object,
 * and is usually sent after receiving an inv packet,
 * after filtering known elements.
 * It can be used to retrieve transactions,
 * but only if they are in the memory pool or
 * relay set - arbitrary access to transactions
 * in the chain is not allowed to avoid having clients
 * start to depend on nodes having full transaction indexes (which modern nodes do not).
 */
data class GetData(
        val count: Int, // Number of inventory entries
        val vectors: List<InventoryVector>
) {
    fun toByteArray(): ByteArray = vectors.let {
        val buffer: ByteBuffer = ByteBuffer.allocate(it.size * (4 + 32))
        it.forEach { iv ->
            buffer.put(iv.toByteArray())
        }
        buffer.array()
    }
}