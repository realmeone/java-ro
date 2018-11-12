package one.realme.krot.net.romtp.body

import java.nio.ByteBuffer

class GetData(private val vectors: List<InventoryVector>) {
    fun toByteArray(): ByteArray = vectors.let {
        val buffer: ByteBuffer = ByteBuffer.allocate(it.size * (4 + 32))
        it.forEach { iv ->
            buffer.put(iv.toByteArray())
        }
        buffer.array()
    }
}