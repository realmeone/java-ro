package one.realme.common

import one.realme.crypto.digest.Ripemd160
import java.security.MessageDigest

object Digests {
    private const val SHA_256 = "SHA-256"
    //    private val SHA_512 = "SHA-512"

    fun ripemd160(input: ByteArray): ByteArray = Ripemd160().digest(input)

    fun sha256(input: ByteArray): ByteArray = newSha256Digest().digest(input)

    fun sha256(b1: ByteArray, b2: ByteArray): ByteArray = newSha256Digest().digest(b1 + b2)

    fun sha256Twice(input: ByteArray): ByteArray {
        val digest = newSha256Digest()
        digest.update(input)
        return digest.digest(digest.digest())
    }

    private fun newSha256Digest() = MessageDigest.getInstance(SHA_256)
}