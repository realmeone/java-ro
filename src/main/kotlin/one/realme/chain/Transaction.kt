package one.realme.chain

/**
 * a transaction in a normal block chain.
 */
class Transaction(
    val from: String,
    val to: String,
    val value: String,
    val timestamp: Long,
    val payload: ByteArray,

    // Signature
    val alg: String,
    val sign: String
) {
    fun hash(): Hash {
        return Hash.empty()
    }
}