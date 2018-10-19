package one.realme.crypto.secp256k1;

import java.math.BigInteger;

import static one.realme.crypto.secp256k1.Secp256k1.*;

public class ECPoint {
    private CurveFp curve;
    private BigInteger x;
    private BigInteger y;

    public ECPoint(CurveFp curve, BigInteger x, BigInteger y) {
        this.curve = curve;
        this.x = x;
        this.y = y;
    }

    public ECPoint twice() {
        final BigInteger p = curve.p();
        final BigInteger a = curve.a();
        //     l = ( ( 3 * self.__x * self.__x + a ) * inverse_mod( 2 * self.__y, p ) ) % p
        final BigInteger l = THREE.multiply(x).multiply(x).add(a).multiply(TWO.multiply(y).modInverse(p)).mod(p);
        //  x3 = (l * l - TWO * x) % p
        final BigInteger x3 = l.multiply(l).subtract(TWO.multiply(x)).mod(p);
        //  y3 = (l * (x - x3) - y) % p
        final BigInteger y3 = l.multiply(x.subtract(x3)).subtract(y).mod(p);
        return new ECPoint(curve, x3, y3);
    }

    public ECPoint add(ECPoint that) {
        if (this.x.equals(that.x)) {
            if (!this.x.add(that.y).mod(this.curve.p()).equals(ZERO)) {
                return this.twice();
            }
        }

        final BigInteger p = this.curve.p();
//     l = ((that.y - this.y) * inverseMod(that.x - this.x, p)) % p
        final BigInteger l = that.y.subtract(this.y).multiply(that.x.subtract(this.x).modInverse(p)).mod(p);
//     x3 = (l * l - this.x - that.x) % p
        final BigInteger x3 = l.multiply(l).subtract(this.x).subtract(that.x).mod(p);
//     y3 = (l * (this.x - x3) - this.y) % p
        final BigInteger y3 = l.multiply(this.x.subtract(x3)).subtract(this.y).mod(p);
        return new ECPoint(this.curve, x3, y3);
    }

    public ECPoint multiply(BigInteger that) {
        BigInteger e = that;
        final BigInteger e3 = THREE.multiply(e);
        final ECPoint negativeSelf = new ECPoint(this.curve, this.x, this.y.negate());

//        BigInteger i = leftMostBit(e3).shiftRight(1);
        // here find e3's MSB, and shift ONE to left to that (MSB - 1) and shift right 1
        // e.g 0010 1000 1111, length is 10, i = 1000 0000
        BigInteger i = ONE.shiftLeft(e3.bitLength() - 1).shiftRight(1);
        ECPoint result = this;
        while (i.compareTo(ONE) > 0) {
            result = result.twice();
            // if ( e3 & i ) != 0 and ( e & i ) == 0: result = result + self
            if (e3.and(i).compareTo(ZERO) != 0 && e.and(i).compareTo(ZERO) == 0) result = result.add(this);
            // if ( e3 & i ) == 0 and ( e & i ) != 0: result = result + negative_self
            if (e3.and(i).compareTo(ZERO) == 0 && e.and(i).compareTo(ZERO) != 0) result = result.add(negativeSelf);
            // i = i / 2
            i = i.shiftRight(1);
        }
        return result;
    }

    public BigInteger x() {
        return x;
    }

    public BigInteger y() {
        return y;
    }
}
