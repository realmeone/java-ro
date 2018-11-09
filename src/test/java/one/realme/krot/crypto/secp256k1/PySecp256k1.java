package one.realme.krot.crypto.secp256k1;

import one.realme.krot.crypto.Hex;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Covert from python implements
 * <p>
 * in python '%' means mod, not reminder
 *
 * @see "https://en.bitcoin.it/wiki/Secp256k1"
 * @see "https://bitcointalk.org/index.php?topic=23241.0"
 * @deprecated use provider BC , because it is faster faster faster!!!!
 */
@Deprecated
public final class PySecp256k1 {
    private final static int RADIX = 16;
    private final static int HASH_DATA_LENGTH = 64;
    private final static int RAW_PUBLIC_KEY_LENGTH = 2 + 64 + 64;
    private final static String RAW_PUBLIC_KEY_PREFIX = "04";

    private final static BigInteger gX = new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16);
    private final static BigInteger gY = new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16);
    private final static BigInteger order = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16);

    final static BigInteger ZERO = BigInteger.ZERO;
    final static BigInteger ONE = BigInteger.valueOf(1);
    final static BigInteger TWO = BigInteger.valueOf(2);
    final static BigInteger THREE = BigInteger.valueOf(3);

    private final ECPoint bigG;
    private final CurveFp curve256;

    public PySecp256k1() {
        curve256 = CurveFp.curve256();
        bigG = new ECPoint(curve256, gX, gY);
    }

    public String computePublicKey(String rawPrivateKey) {
        BigInteger s = new BigInteger(rawPrivateKey, RADIX);
        ECPoint point = bigG.multiply(s);
        return RAW_PUBLIC_KEY_PREFIX + adjustTo64(point.x()) + adjustTo64(point.y());
    }

    public String sign(String hash, String rawPrivateKey) {
        if (hash.length() != HASH_DATA_LENGTH)
            throw new IllegalArgumentException("Not a valid data, have you passed sha256 hash data?");

        BigInteger data = new BigInteger(hash, RADIX);
        BigInteger secret = new BigInteger(rawPrivateKey, RADIX);
        BigInteger n = order(); //        n = G.order()

        BigInteger k = new BigInteger(n.bitLength(), new SecureRandom()).mod(n); //        k = random_k % n  random in range 1 to n
        ECPoint p1 = bigG.multiply(k); //        p1 = k * G
        BigInteger r = p1.x(); //        r = p1.x()
        if (r.equals(ZERO))
            throw new IllegalArgumentException("amazingly unlucky random number r"); //        if r == 0: raise RuntimeError, "amazingly unlucky random number r"
        BigInteger s = k.modInverse(n).multiply(data.add(secret.multiply(r).mod(n))).mod(n);  //        s = ( inverse_mod( k, n ) * ( hash + ( self.secret_multiplier * r ) % n ) ) % n
        if (s.equals(ZERO))
            throw new IllegalArgumentException("amazingly unlucky random number s"); //        if s == 0: raise RuntimeError, "amazingly unlucky random number s"
        return Hex.INSTANCE.encode(new Signature(r, s).der());
    }

    public boolean verify(String hash, String signedData, String rawPublicKey) {
        try {
            // verify site
            if (hash.length() != HASH_DATA_LENGTH) throw new IllegalArgumentException("Not a valid data, have you passed sha256 hash data?");
            if (signedData.length() > 520) throw new IllegalArgumentException("Signed data is too big");
            if (rawPublicKey.length() != RAW_PUBLIC_KEY_LENGTH) throw new IllegalArgumentException("Public key is too big");
            if (!rawPublicKey.startsWith(RAW_PUBLIC_KEY_PREFIX)) throw new IllegalArgumentException("Not a valid public key");

            // decode data from hex
            BigInteger data = new BigInteger(hash, RADIX);
            Signature signature = new Signature(Hex.INSTANCE.decode(signedData));
            // point of public key
            rawPublicKey = rawPublicKey.substring(2);
            ECPoint pubKey = new ECPoint(curve256,
                    new BigInteger(rawPublicKey.substring(0, 64), RADIX),
                    new BigInteger(rawPublicKey.substring(64), RADIX)
            );

            BigInteger r = signature.r();             //            r = signature.r
            BigInteger s = signature.s();             //            s = signature.s
            BigInteger n = order();             //            n = G.order()

            // verify
            if (r.compareTo(ONE) < 0 || r.compareTo(n.subtract(ONE)) > 0) return false;
            if (s.compareTo(ONE) < 0 || s.compareTo(n.subtract(ONE)) > 0) return false;
            BigInteger c = s.modInverse(n); //            c = inverse_mod( s, n )
            BigInteger u1 = data.multiply(c).mod(n); //            u1 = ( hash * c ) % n
            BigInteger u2 = r.multiply(c).mod(n); //            u2 = ( r * c ) % n
            ECPoint xy = bigG.multiply(u1).add(pubKey.multiply(u2)); //            xy = u1 * G + u2 * self.point
            BigInteger v = xy.x().mod(n); //            v = xy.x() % n
            return v.equals(r); //            return v == r
        } catch (Exception ignore) {
            return false;
        }
    }


//    public String sign(String hex, String rawPrivateKey) {
//
//    }

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
