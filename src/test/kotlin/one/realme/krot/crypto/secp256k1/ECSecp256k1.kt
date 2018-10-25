package one.realme.krot.crypto.secp256k1

import one.realme.krot.crypto.encoding.Hex
import java.math.BigInteger
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Signature
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec

/**
 * The elliptic curve domain parameters over Fp associated with a Koblitz curve secp256k1 are specified by the sextuple T = (p,a,b,G,n,h) where the finite field Fp is defined by:
 * p = FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFE FFFFFC2F = 2256 - 232 - 29 - 28 - 27 - 26 - 24 - 1
 *
 * The curve E: y2 = x3+ax+b over Fp is defined by:
 * a = 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000
 * b = 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000007
 * The base point G in compressed form is:
 * G = 04 79BE667E F9DCBBAC 55A06295 CE870B07 029BFCDB 2DCE28D9 59F2815B 16F81798 483ADA77 26A3C465 5DA4FBFC 0E1108A8 FD17B448 A6855419 9C47D08F FB10D4B8
 * Finally the order n of G and the cofactor are:
 * n = FFFFFFFF FFFFFFFF FFFFFFFF FFFFFFFE BAAEDCE6 AF48A03B BFD25E8C D0364141
 * h = 01
 *
 * ----------------------------------------------------------------------------------
 * In oracle jvm 7+
 * p = key.params.curve.a
 * b = key.params.curve.b
 * n = key.params.order
 * p = (key.params.curve.field as ECFieldFp).p
 *
 * so we can use oracle jvm secp256k1 curve
 */
object ECSecp256k1 {
    // the data already SHA256 hashed. so there is no need for SHA256withECDSA
    private const val ALG_SIGN = "NONEwithECDSA"
    private const val ALG = "EC"
    private const val CURVE = "secp256k1"
    private const val X509_HEAD = "3056301006072a8648ce3d020106052b8104000a03420004"
    private const val PKCS8_HEAD = "303e020100301006072a8648ce3d020106052b8104000a042730250201010420"

    fun newKeyPair(): Pair<String, String> {
        val keyPairGenerator = KeyPairGenerator.getInstance(ALG)
        val curve = ECGenParameterSpec(CURVE)
        keyPairGenerator.initialize(curve, SecureRandom())
        val keyPair = keyPairGenerator.generateKeyPair()
        val pri = keyPair.private as ECPrivateKey
        val pub = keyPair.public as ECPublicKey

        // gX, gY, s need fix to 64 size
        val gX = adjustTo64(pub.w.affineX)
        val gY = adjustTo64(pub.w.affineY)
        val s = adjustTo64(pri.s)
        return Pair(s, "04$gX$gY")
    }


    fun sign(data: String, sec: String): String {
        val pkcs8Encode = Hex.decode(PKCS8_HEAD + sec)
        val keyFactory = KeyFactory.getInstance(ALG)
        val priKey = keyFactory.generatePrivate(PKCS8EncodedKeySpec(pkcs8Encode)) as ECPrivateKey
        // verify
        val signature = Signature.getInstance(ALG_SIGN)
        signature.initSign(priKey)
        signature.update(Hex.decode(data))
        return Hex.encode(signature.sign())
    }

    /**
     * @param data Hex data
     * @param sig Hex sig
     * @param pub public key with "04" prefix
     */
    fun verify(data: String, sig: String, pub: String): Boolean {
        return try {
            // verify site
            require(data.length == 64) { "Not a valid data, have you passed sha256 hash data?" }
            require(sig.length <= 520) { "Signed data is too big." }
            require(pub.length <= 520) { "Public key is too big." }
            require(pub.startsWith("04")) { "Not a valid public key" }

            // decode data from hex
            val pubX509Encode = Hex.decode(X509_HEAD + pub.removePrefix("04"))
            val dataBytes = Hex.decode(data)
            val sigBytes = Hex.decode(sig)

            // gen  public key
            val keyFactory = KeyFactory.getInstance(ALG)
            val pubKey = keyFactory.generatePublic(X509EncodedKeySpec(pubX509Encode))

            // verify
            val signature = Signature.getInstance(ALG_SIGN)
            signature.initVerify(pubKey)
            signature.update(dataBytes)

            signature.verify(sigBytes)
        } catch (ignore: Exception) {
            false
        }
    }

    private fun adjustTo64(s: BigInteger): String = String.format("%064x", s)
}