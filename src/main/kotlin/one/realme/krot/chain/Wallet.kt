package one.realme.krot.chain

import one.realme.krot.crypto.Secp256k1

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