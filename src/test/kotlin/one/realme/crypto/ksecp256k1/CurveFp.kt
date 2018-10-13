package one.realme.crypto.ksecp256k1

import java.math.BigInteger

class CurveFp(
        val p: BigInteger,
        val a: BigInteger,
        val b: BigInteger,
        val order: BigInteger
) {
    companion object {
        fun curve256(): CurveFp =
                CurveFp(
                        p = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16),
                        b = BigInteger.valueOf(7),
                        a = BigInteger.ZERO,
                        order = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16)
                )
    }

    // y * y = (x * x * x) + (a * x) + b
    fun containsPoint(x: BigInteger, y: BigInteger): Boolean {
        val left = y * y
        val right = (x * x * x) + this.a * x + this.b
        return (left - right).mod(this.p) == BigInteger.ZERO
    }
}