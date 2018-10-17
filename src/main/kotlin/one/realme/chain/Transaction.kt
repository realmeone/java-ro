package one.realme.chain

import one.realme.common.UnixTime

/**
 * a transaction in a normal block chain.
 */
class Transaction(
        val from: Address,
        val to: Address,
        val value: Coin,
        val timestamp: UnixTime,
        val payload: ByteArray,

        // Signature
        val alg: String,
        val sign: String
) {
    fun hash(): Hash {
        return Hash.empty()
    }
}