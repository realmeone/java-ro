package one.realme.crypto.secp256k1;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * https://tools.ietf.org/html/rfc3278#section-8.2
 * <p>
 * When using ECDSA with SignedData, ECDSA signatures are encoded using
 * the type:
 * <p>
 * ECDSA-Sig-Value ::= SEQUENCE {
 * r INTEGER,
 * s INTEGER }
 */
class Signature {
    private final BigInteger r;
    private final BigInteger s;

    Signature(byte[] rawBytes) {
        try {
            // exact r & s
            int startR = (rawBytes[1] & 0x80) != 0 ? 3 : 2;
            int lengthR = rawBytes[startR + 1];
            int startS = startR + 2 + lengthR;
            int lengthS = rawBytes[startS + 1];
            s = new BigInteger(Arrays.copyOfRange(rawBytes, startS + 2, startS + 2 + lengthS));
            r = new BigInteger(Arrays.copyOfRange(rawBytes, startR + 2, startR + 2 + lengthR));
        } catch (Exception e) {
            throw new IllegalArgumentException("not a valid signature.");
        }
    }

    Signature(BigInteger r, BigInteger s) {
        this.r = r;
        this.s = s;
    }

    /**
     * der:
     * 0x30 b1 0x02 b2 (vr) 0x02 b3 (vs)
     * <p>
     * where:
     * <p>
     * b1 is a single byte value, equal to the length, in bytes, of the remaining list of bytes (from the first 0x02 to the end of the encoding);
     * b2 is a single byte value, equal to the length, in bytes, of (vr);
     * b3 is a single byte value, equal to the length, in bytes, of (vs);
     * (vr) is the signed big-endian encoding of the value "r", of minimal length;
     * (vs) is the signed big-endian encoding of the value "s", of minimal length.
     */
    byte[] der() {
        byte[] rBytes = r.toByteArray();
        byte[] sBytes = s.toByteArray();
        int vr = rBytes.length;
        int vs = sBytes.length;
        int b1 = 1 + 1 + vr + 1 + 1 + vs; // b1 = 1(0x02) +  1(b2) + vr + 1(0x02) + 1(b3) + vs
        int derLength = 1 + 1 + b1;  // derL = 1(0x30) + 1(b1) + b1L

        byte[] data = new byte[derLength];
        data[0] = 0x30;
        data[1] = (byte) b1;
        data[2] = 0x02;
        data[3] = (byte) vr;
        System.arraycopy(rBytes, 0, data, 4, vr);
        data[4 + vr] = 0x02;
        data[5 + vr] = (byte) vs;
        System.arraycopy(sBytes, 0, data, 6 + vr, vs);

        return data;
    }

    BigInteger r() {
        return r;
    }

    BigInteger s() {
        return s;
    }
}
