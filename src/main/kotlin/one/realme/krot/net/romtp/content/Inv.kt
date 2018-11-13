package one.realme.krot.net.romtp.content

import one.realme.krot.common.toByteArray
import java.nio.ByteBuffer

/**
 * Allows a node to advertise its knowledge of one or more objects. It can be received unsolicited, or in reply to getblocks.
 */
class Inv(
        val count: Int, // Number of inventory entries
        val vectors: List<InventoryVector> // inventory vectors
) {
    fun toByteArray(): ByteArray = count.toByteArray() + vectors.let {
        val buffer: ByteBuffer = ByteBuffer.allocate(it.size * (4 + 32))
        it.forEach { iv ->
            buffer.put(iv.toByteArray())
        }
        buffer.array()
    }
}