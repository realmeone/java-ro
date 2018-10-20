package one.realme.chain

import one.realme.common.UnixTime
import one.realme.common.toBytesLE
import one.realme.crypto.sha256Twice
import java.nio.ByteBuffer
import java.util.*

/**
 * a transaction in a normal block chain.
 */
class Transaction private constructor(
        val from: Address,
        val to: Address,
        val amount: Coin,
        val timestamp: UnixTime
) {
    companion object {
        fun create(from: Address, to: Address, amount: Coin): Transaction = Transaction(from, to, amount, UnixTime.now())
    }

    private val nVersion = 1
    private val vin = Vector<TxIn>()
    private val vout = Vector<TxOut>()

    fun isNull(): Boolean = vin.isEmpty() && vout.isEmpty()
    fun vinSize(): Int = vin.size
    fun voutSize(): Int = vout.size

    fun hash(): Hash {
        val buffer = ByteBuffer.allocate(66)
                .put(nVersion.toBytesLE()) // 4 bytes
                .put(from.toBytesLE()) // 25 bytes
                .put(to.toBytesLE()) // 25 bytes
                .put(amount.toBytesLE()) // 8 bytes
                .put(timestamp.toBytesLE()) // 4 bytes
        return Hash.fromBytes(buffer.array().sha256Twice())
    }
}