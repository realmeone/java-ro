package one.realme.krot.common.primitive

import one.realme.krot.common.lang.UnixTime
import one.realme.krot.common.crypto.Secp256k1
import one.realme.krot.common.digest.sha256Twice
import java.nio.ByteBuffer

/**
 * a transaction in a normal block chain.
 */
class Transaction(
        val from: Address,
        val to: Address,
        val amount: Coin,
        val payload: ByteArray = ByteArray(0),
        val timestamp: UnixTime = UnixTime.now(),
        var signature: String = ""
) {
    val hash by lazy {
        Hash.fromBytes(toByteArray().sha256Twice())
    }

    companion object {
        fun coinbase(to: Address): Transaction = Transaction(Address.empty(), to, Coin.BASE_REWARD)
    }

    fun sign(privateKey: String) {
        signature = Secp256k1.sign(hash.toString(), privateKey)
    }

    fun isValid(): Boolean =
            if (isCoinbase()) true
            else try {
                Secp256k1.verify(hash.toString(), signature, from.toString())
            } catch (ex: Exception) {
                false
            }

    fun isCoinbase(): Boolean = Address.empty() == to


    fun toByteArray(): ByteArray =
            ByteBuffer.allocate(62 + payload.size)
                    .put(from.toByteArray()) // 25 bytes
                    .put(to.toByteArray()) // 25 bytes
                    .put(amount.toByteArray()) // 8 bytes
                    .put(timestamp.toByteArray()) // 4 bytes
                    .put(payload).array()


    override fun toString(): String {
        return "Transaction(from=$from, to=$to, amount=$amount, timestamp=$timestamp)"
    }


}