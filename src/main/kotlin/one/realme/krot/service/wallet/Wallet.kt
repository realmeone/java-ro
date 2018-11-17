package one.realme.krot.service.wallet

import one.realme.krot.common.crypto.Secp256k1
import one.realme.krot.common.primitive.Address

class Wallet(
        val privateKey: String,
        val publicKey: String
) {
    companion object {
        fun create(): Wallet {
            val keyPair = Secp256k1.newKeyPair()
            val pubKey = keyPair.second
            val priKey = keyPair.first
            return Wallet(priKey, pubKey)
        }
    }

    val address = Address(publicKey, 0)
}