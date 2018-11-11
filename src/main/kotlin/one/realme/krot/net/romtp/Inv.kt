package one.realme.krot.net.romtp

import one.realme.krot.chain.BlockChain
import one.realme.krot.chain.Hash
import one.realme.krot.common.toInt

class Inv(
        val from: Hash, // 32 bytes
        val type: Int) {
    companion object {
        const val TYPE_BLOCK = 1
        const val TYPE_TX = 2

        fun fromBytes(raw: ByteArray): Inv {
            val fromBytes = raw.copyOfRange(0, 32)
            val typeBytes = raw.copyOfRange(32, 36)
            return Inv(
                    Hash.fromBytes(fromBytes),
                    typeBytes.toInt()
            )
        }

        fun handleInv(raw: ByteArray, chain: BlockChain) {
            val inv = Inv.fromBytes(raw)
            when (inv.type) {
                TYPE_BLOCK -> {

                }
                TYPE_TX -> {

                }
                else -> {

                }
            }
        }
    }
}