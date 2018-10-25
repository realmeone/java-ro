package one.realme.krot.chain

import one.realme.krot.common.UnixTime
import one.realme.krot.common.toBytesLE
import one.realme.krot.crypto.sha256Twice
import java.nio.ByteBuffer

/**
 * a transaction in a normal block chain.
 */
class Transaction(
        val from: Address,
        val to: Address,
        val amount: Coin,
        val payload: ByteArray = ByteArray(0),
        val timestamp: UnixTime = UnixTime.now()
) {
    private val nVersion = 1

    companion object {
        fun coinbase(to: Address): Transaction = Transaction(to, to, Coin.BASE_REWARD)
    }

    fun hash(): Hash {
        val buffer = ByteBuffer.allocate(66 + payload.size)
                .put(nVersion.toBytesLE()) // 4 bytes
                .put(from.toBytesLE()) // 25 bytes
                .put(to.toBytesLE()) // 25 bytes
                .put(amount.toBytesLE()) // 8 bytes
                .put(timestamp.toBytesLE()) // 4 bytes
                .put(payload)  // calculate
        return Hash.fromBytes(buffer.array().sha256Twice())
    }

    override fun toString(): String {
        return "Transaction(from=$from, to=$to, amount=$amount, timestamp=$timestamp, nVersion=$nVersion)"
    }


}