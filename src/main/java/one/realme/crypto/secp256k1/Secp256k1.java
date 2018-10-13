package one.realme.crypto.secp256k1;

import java.math.BigInteger;

/**
 * Covert from python implements
 * <p>
 * in python '%' means mod, not reminder
 *
 * @see "https://en.bitcoin.it/wiki/Secp256k1"
 * @see "https://bitcointalk.org/index.php?topic=23241.0"
 */
public final class Secp256k1 {
    private final static int RADIX = 16;
    private final static int KEY_LENGTH = 64;
    private final static BigInteger gX = new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16);
    private final static BigInteger gY = new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16);
    private final static BigInteger order = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16);
    private final static String RAW_PUBLIC_KEY_PREFIX = "04";
    private final ECPoint bigG;
    private final CurveFp curve256;

    public Secp256k1() {
        curve256 = CurveFp.curve256();
        bigG = new ECPoint(curve256, gX, gY);
    }

    public String computePublicKey(String rawPrivateKey) {
        BigInteger s = new BigInteger(rawPrivateKey, RADIX);
        ECPoint point = bigG.multiply(s);
        return RAW_PUBLIC_KEY_PREFIX + adjustTo64(point.x()) + adjustTo64(point.y());
    }

    public static BigInteger order() {
        return order;
    }

    public CurveFp curveFp() {
        return curve256;
    }

    private String adjustTo64(BigInteger s) {
        return String.format("%064x", s);
    }
}
