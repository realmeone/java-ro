package one.realme.chain

import one.realme.common.UnixTime
import java.util.*

/**
 * a transaction in a normal block chain.
 */
class Transaction(
        val from: Address,
        val to: Address,
        val amount: Coin,
        val timestamp: UnixTime,
        // Signature
        val alg: String,
        val sign: String
) {
    companion object {
        const val CURRENT_VERSION = 1
    }

    private val nVersion = CURRENT_VERSION
    private val vin = Vector<TxIn>()
    private val vout = Vector<TxOut>()

    fun isNull(): Boolean = vin.isEmpty() && vout.isEmpty()
    fun vinSize(): Int = vin.size
    fun voutSize(): Int = vout.size

    fun hash(): Hash {
        return Hash.empty()
    }
}