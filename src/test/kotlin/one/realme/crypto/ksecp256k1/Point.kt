package one.realme.crypto.ksecp256k1

import java.math.BigInteger

class Point(
        val curve: CurveFp,
        val x: BigInteger,
        val y: BigInteger) {

    init {
        require(curve.containsPoint(x, y)) { "point not in the curve" }
    }

    companion object {
        private val two = BigInteger.valueOf(2)
        private val three = BigInteger.valueOf(3)
        private val zero = BigInteger.ZERO
        private val one = BigInteger.ONE
    }

    fun double(): Point {
        val p = curve.p
        val a = curve.a

        val l = ((three * x * x + a) * (two * y).modInverse(p)).mod(p)
        val x3 = (l * l - two * x).mod(p)
        val y3 = (l * (x - x3) - y).mod(p)
        return Point(curve, x3, y3)
    }

    override fun toString(): String = "($x,$y)"

    operator fun plus(that: Point): Point {
        if (this.x == that.x)
            if ((this.y + that.y).mod(this.curve.p) != zero)
                return this.double()

        val p = this.curve.p
        val l = ((that.y - this.y) * (that.x - this.x).modInverse(p)).mod(p)
        val x3 = (l * l - this.x - that.x).mod(p)
        val y3 = (l * (this.x - x3) - this.y).mod(p)
        return Point(this.curve, x3, y3)
    }

    operator fun times(that: BigInteger): Point {
        val e = that
        require(e > zero) { "that must bigger than zero" }
        val e3 = three * e
        val negativeSelf = Point(this.curve, this.x, -this.y)
//        var i = leftMostBit(e3) / two
        // find highest bit and shift right 1
        var i = one.shl(e3.bitLength() - 1) shr 1
        var result = this
        while (i > one) {
            result = result.double()
            if ((e3 and i) != zero && (e and i) == zero) result += this
            if ((e3 and i) == zero && (e and i) != zero) result += negativeSelf
            i /= two
        }
        return result
    }

    // help functions
    private fun leftMostBit(x: BigInteger): BigInteger {
        require(x > zero)
        var result: BigInteger = one
        while (result <= x) result = two * result
        return result / two
    }
}