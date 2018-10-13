package one.realme.crypto.secp256k1;

import java.math.BigInteger;

public class CurveFp {
    private BigInteger p, a, b;

    public CurveFp(BigInteger p, BigInteger a, BigInteger b) {
        this.a = a;
        this.b = b;
        this.p = p;
    }

    public BigInteger a() {
        return a;
    }

    public BigInteger b() {
        return b;
    }

    public BigInteger p() {
        return p;
    }

    public static CurveFp curve256() {
        return new CurveFp(
                new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16),
                BigInteger.ZERO,
                BigInteger.valueOf(7)
        );
    }
}
