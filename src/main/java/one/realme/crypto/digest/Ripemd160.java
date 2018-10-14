package one.realme.crypto.digest;

/**
 * ripemd 160 hash
 * <p>
 * Covert from bitcoin source hashBytes.h and hashBytes.cpp
 *
 * and this is faster than BC implements, see the test
 */
public final class Ripemd160 {

    public static byte[] hashBytes(final byte[] input) {
        return new Ripemd160().digest(input);
    }

    private int h0, h1, h2, h3, h4;

    private int[] x = new int[16];
    private final byte[] xBuf = new byte[4];
    private int xOff;
    private int xBufOff;
    private long byteCount;

    public Ripemd160() {
        reset();
    }


    private void processBlock() {
        int a, aa;
        int b, bb;
        int c, cc;
        int d, dd;
        int e, ee;

        a = aa = h0;
        b = bb = h1;
        c = cc = h2;
        d = dd = h3;
        e = ee = h4;

        //
        // Rounds 1 - 16
        //
        // left
        a = rol(a + f1(b, c, d) + x[0], 11) + e;
        c = rol(c, 10);
        e = rol(e + f1(a, b, c) + x[1], 14) + d;
        b = rol(b, 10);
        d = rol(d + f1(e, a, b) + x[2], 15) + c;
        a = rol(a, 10);
        c = rol(c + f1(d, e, a) + x[3], 12) + b;
        e = rol(e, 10);
        b = rol(b + f1(c, d, e) + x[4], 5) + a;
        d = rol(d, 10);
        a = rol(a + f1(b, c, d) + x[5], 8) + e;
        c = rol(c, 10);
        e = rol(e + f1(a, b, c) + x[6], 7) + d;
        b = rol(b, 10);
        d = rol(d + f1(e, a, b) + x[7], 9) + c;
        a = rol(a, 10);
        c = rol(c + f1(d, e, a) + x[8], 11) + b;
        e = rol(e, 10);
        b = rol(b + f1(c, d, e) + x[9], 13) + a;
        d = rol(d, 10);
        a = rol(a + f1(b, c, d) + x[10], 14) + e;
        c = rol(c, 10);
        e = rol(e + f1(a, b, c) + x[11], 15) + d;
        b = rol(b, 10);
        d = rol(d + f1(e, a, b) + x[12], 6) + c;
        a = rol(a, 10);
        c = rol(c + f1(d, e, a) + x[13], 7) + b;
        e = rol(e, 10);
        b = rol(b + f1(c, d, e) + x[14], 9) + a;
        d = rol(d, 10);
        a = rol(a + f1(b, c, d) + x[15], 8) + e;
        c = rol(c, 10);

        // right
        aa = rol(aa + f5(bb, cc, dd) + x[5] + 0x50a28be6, 8) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f5(aa, bb, cc) + x[14] + 0x50a28be6, 9) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f5(ee, aa, bb) + x[7] + 0x50a28be6, 9) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f5(dd, ee, aa) + x[0] + 0x50a28be6, 11) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f5(cc, dd, ee) + x[9] + 0x50a28be6, 13) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f5(bb, cc, dd) + x[2] + 0x50a28be6, 15) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f5(aa, bb, cc) + x[11] + 0x50a28be6, 15) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f5(ee, aa, bb) + x[4] + 0x50a28be6, 5) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f5(dd, ee, aa) + x[13] + 0x50a28be6, 7) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f5(cc, dd, ee) + x[6] + 0x50a28be6, 7) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f5(bb, cc, dd) + x[15] + 0x50a28be6, 8) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f5(aa, bb, cc) + x[8] + 0x50a28be6, 11) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f5(ee, aa, bb) + x[1] + 0x50a28be6, 14) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f5(dd, ee, aa) + x[10] + 0x50a28be6, 14) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f5(cc, dd, ee) + x[3] + 0x50a28be6, 12) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f5(bb, cc, dd) + x[12] + 0x50a28be6, 6) + ee;
        cc = rol(cc, 10);

        //
        // Rounds 16-31
        //
        // left
        e = rol(e + f2(a, b, c) + x[7] + 0x5a827999, 7) + d;
        b = rol(b, 10);
        d = rol(d + f2(e, a, b) + x[4] + 0x5a827999, 6) + c;
        a = rol(a, 10);
        c = rol(c + f2(d, e, a) + x[13] + 0x5a827999, 8) + b;
        e = rol(e, 10);
        b = rol(b + f2(c, d, e) + x[1] + 0x5a827999, 13) + a;
        d = rol(d, 10);
        a = rol(a + f2(b, c, d) + x[10] + 0x5a827999, 11) + e;
        c = rol(c, 10);
        e = rol(e + f2(a, b, c) + x[6] + 0x5a827999, 9) + d;
        b = rol(b, 10);
        d = rol(d + f2(e, a, b) + x[15] + 0x5a827999, 7) + c;
        a = rol(a, 10);
        c = rol(c + f2(d, e, a) + x[3] + 0x5a827999, 15) + b;
        e = rol(e, 10);
        b = rol(b + f2(c, d, e) + x[12] + 0x5a827999, 7) + a;
        d = rol(d, 10);
        a = rol(a + f2(b, c, d) + x[0] + 0x5a827999, 12) + e;
        c = rol(c, 10);
        e = rol(e + f2(a, b, c) + x[9] + 0x5a827999, 15) + d;
        b = rol(b, 10);
        d = rol(d + f2(e, a, b) + x[5] + 0x5a827999, 9) + c;
        a = rol(a, 10);
        c = rol(c + f2(d, e, a) + x[2] + 0x5a827999, 11) + b;
        e = rol(e, 10);
        b = rol(b + f2(c, d, e) + x[14] + 0x5a827999, 7) + a;
        d = rol(d, 10);
        a = rol(a + f2(b, c, d) + x[11] + 0x5a827999, 13) + e;
        c = rol(c, 10);
        e = rol(e + f2(a, b, c) + x[8] + 0x5a827999, 12) + d;
        b = rol(b, 10);

        // right
        ee = rol(ee + f4(aa, bb, cc) + x[6] + 0x5c4dd124, 9) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f4(ee, aa, bb) + x[11] + 0x5c4dd124, 13) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f4(dd, ee, aa) + x[3] + 0x5c4dd124, 15) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f4(cc, dd, ee) + x[7] + 0x5c4dd124, 7) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f4(bb, cc, dd) + x[0] + 0x5c4dd124, 12) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f4(aa, bb, cc) + x[13] + 0x5c4dd124, 8) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f4(ee, aa, bb) + x[5] + 0x5c4dd124, 9) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f4(dd, ee, aa) + x[10] + 0x5c4dd124, 11) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f4(cc, dd, ee) + x[14] + 0x5c4dd124, 7) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f4(bb, cc, dd) + x[15] + 0x5c4dd124, 7) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f4(aa, bb, cc) + x[8] + 0x5c4dd124, 12) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f4(ee, aa, bb) + x[12] + 0x5c4dd124, 7) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f4(dd, ee, aa) + x[4] + 0x5c4dd124, 6) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f4(cc, dd, ee) + x[9] + 0x5c4dd124, 15) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f4(bb, cc, dd) + x[1] + 0x5c4dd124, 13) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f4(aa, bb, cc) + x[2] + 0x5c4dd124, 11) + dd;
        bb = rol(bb, 10);

        //
        // Rounds 32-47
        //
        // left
        d = rol(d + f3(e, a, b) + x[3] + 0x6ed9eba1, 11) + c;
        a = rol(a, 10);
        c = rol(c + f3(d, e, a) + x[10] + 0x6ed9eba1, 13) + b;
        e = rol(e, 10);
        b = rol(b + f3(c, d, e) + x[14] + 0x6ed9eba1, 6) + a;
        d = rol(d, 10);
        a = rol(a + f3(b, c, d) + x[4] + 0x6ed9eba1, 7) + e;
        c = rol(c, 10);
        e = rol(e + f3(a, b, c) + x[9] + 0x6ed9eba1, 14) + d;
        b = rol(b, 10);
        d = rol(d + f3(e, a, b) + x[15] + 0x6ed9eba1, 9) + c;
        a = rol(a, 10);
        c = rol(c + f3(d, e, a) + x[8] + 0x6ed9eba1, 13) + b;
        e = rol(e, 10);
        b = rol(b + f3(c, d, e) + x[1] + 0x6ed9eba1, 15) + a;
        d = rol(d, 10);
        a = rol(a + f3(b, c, d) + x[2] + 0x6ed9eba1, 14) + e;
        c = rol(c, 10);
        e = rol(e + f3(a, b, c) + x[7] + 0x6ed9eba1, 8) + d;
        b = rol(b, 10);
        d = rol(d + f3(e, a, b) + x[0] + 0x6ed9eba1, 13) + c;
        a = rol(a, 10);
        c = rol(c + f3(d, e, a) + x[6] + 0x6ed9eba1, 6) + b;
        e = rol(e, 10);
        b = rol(b + f3(c, d, e) + x[13] + 0x6ed9eba1, 5) + a;
        d = rol(d, 10);
        a = rol(a + f3(b, c, d) + x[11] + 0x6ed9eba1, 12) + e;
        c = rol(c, 10);
        e = rol(e + f3(a, b, c) + x[5] + 0x6ed9eba1, 7) + d;
        b = rol(b, 10);
        d = rol(d + f3(e, a, b) + x[12] + 0x6ed9eba1, 5) + c;
        a = rol(a, 10);

        // right
        dd = rol(dd + f3(ee, aa, bb) + x[15] + 0x6d703ef3, 9) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f3(dd, ee, aa) + x[5] + 0x6d703ef3, 7) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f3(cc, dd, ee) + x[1] + 0x6d703ef3, 15) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f3(bb, cc, dd) + x[3] + 0x6d703ef3, 11) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f3(aa, bb, cc) + x[7] + 0x6d703ef3, 8) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f3(ee, aa, bb) + x[14] + 0x6d703ef3, 6) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f3(dd, ee, aa) + x[6] + 0x6d703ef3, 6) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f3(cc, dd, ee) + x[9] + 0x6d703ef3, 14) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f3(bb, cc, dd) + x[11] + 0x6d703ef3, 12) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f3(aa, bb, cc) + x[8] + 0x6d703ef3, 13) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f3(ee, aa, bb) + x[12] + 0x6d703ef3, 5) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f3(dd, ee, aa) + x[2] + 0x6d703ef3, 14) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f3(cc, dd, ee) + x[10] + 0x6d703ef3, 13) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f3(bb, cc, dd) + x[0] + 0x6d703ef3, 13) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f3(aa, bb, cc) + x[4] + 0x6d703ef3, 7) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f3(ee, aa, bb) + x[13] + 0x6d703ef3, 5) + cc;
        aa = rol(aa, 10);

        //
        // Rounds 48-63
        //
        // left
        c = rol(c + f4(d, e, a) + x[1] + 0x8f1bbcdc, 11) + b;
        e = rol(e, 10);
        b = rol(b + f4(c, d, e) + x[9] + 0x8f1bbcdc, 12) + a;
        d = rol(d, 10);
        a = rol(a + f4(b, c, d) + x[11] + 0x8f1bbcdc, 14) + e;
        c = rol(c, 10);
        e = rol(e + f4(a, b, c) + x[10] + 0x8f1bbcdc, 15) + d;
        b = rol(b, 10);
        d = rol(d + f4(e, a, b) + x[0] + 0x8f1bbcdc, 14) + c;
        a = rol(a, 10);
        c = rol(c + f4(d, e, a) + x[8] + 0x8f1bbcdc, 15) + b;
        e = rol(e, 10);
        b = rol(b + f4(c, d, e) + x[12] + 0x8f1bbcdc, 9) + a;
        d = rol(d, 10);
        a = rol(a + f4(b, c, d) + x[4] + 0x8f1bbcdc, 8) + e;
        c = rol(c, 10);
        e = rol(e + f4(a, b, c) + x[13] + 0x8f1bbcdc, 9) + d;
        b = rol(b, 10);
        d = rol(d + f4(e, a, b) + x[3] + 0x8f1bbcdc, 14) + c;
        a = rol(a, 10);
        c = rol(c + f4(d, e, a) + x[7] + 0x8f1bbcdc, 5) + b;
        e = rol(e, 10);
        b = rol(b + f4(c, d, e) + x[15] + 0x8f1bbcdc, 6) + a;
        d = rol(d, 10);
        a = rol(a + f4(b, c, d) + x[14] + 0x8f1bbcdc, 8) + e;
        c = rol(c, 10);
        e = rol(e + f4(a, b, c) + x[5] + 0x8f1bbcdc, 6) + d;
        b = rol(b, 10);
        d = rol(d + f4(e, a, b) + x[6] + 0x8f1bbcdc, 5) + c;
        a = rol(a, 10);
        c = rol(c + f4(d, e, a) + x[2] + 0x8f1bbcdc, 12) + b;
        e = rol(e, 10);

        // right
        cc = rol(cc + f2(dd, ee, aa) + x[8] + 0x7a6d76e9, 15) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f2(cc, dd, ee) + x[6] + 0x7a6d76e9, 5) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f2(bb, cc, dd) + x[4] + 0x7a6d76e9, 8) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f2(aa, bb, cc) + x[1] + 0x7a6d76e9, 11) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f2(ee, aa, bb) + x[3] + 0x7a6d76e9, 14) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f2(dd, ee, aa) + x[11] + 0x7a6d76e9, 14) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f2(cc, dd, ee) + x[15] + 0x7a6d76e9, 6) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f2(bb, cc, dd) + x[0] + 0x7a6d76e9, 14) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f2(aa, bb, cc) + x[5] + 0x7a6d76e9, 6) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f2(ee, aa, bb) + x[12] + 0x7a6d76e9, 9) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f2(dd, ee, aa) + x[2] + 0x7a6d76e9, 12) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f2(cc, dd, ee) + x[13] + 0x7a6d76e9, 9) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f2(bb, cc, dd) + x[9] + 0x7a6d76e9, 12) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f2(aa, bb, cc) + x[7] + 0x7a6d76e9, 5) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f2(ee, aa, bb) + x[10] + 0x7a6d76e9, 15) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f2(dd, ee, aa) + x[14] + 0x7a6d76e9, 8) + bb;
        ee = rol(ee, 10);

        //
        // Rounds 64-79
        //
        // left
        b = rol(b + f5(c, d, e) + x[4] + 0xa953fd4e, 9) + a;
        d = rol(d, 10);
        a = rol(a + f5(b, c, d) + x[0] + 0xa953fd4e, 15) + e;
        c = rol(c, 10);
        e = rol(e + f5(a, b, c) + x[5] + 0xa953fd4e, 5) + d;
        b = rol(b, 10);
        d = rol(d + f5(e, a, b) + x[9] + 0xa953fd4e, 11) + c;
        a = rol(a, 10);
        c = rol(c + f5(d, e, a) + x[7] + 0xa953fd4e, 6) + b;
        e = rol(e, 10);
        b = rol(b + f5(c, d, e) + x[12] + 0xa953fd4e, 8) + a;
        d = rol(d, 10);
        a = rol(a + f5(b, c, d) + x[2] + 0xa953fd4e, 13) + e;
        c = rol(c, 10);
        e = rol(e + f5(a, b, c) + x[10] + 0xa953fd4e, 12) + d;
        b = rol(b, 10);
        d = rol(d + f5(e, a, b) + x[14] + 0xa953fd4e, 5) + c;
        a = rol(a, 10);
        c = rol(c + f5(d, e, a) + x[1] + 0xa953fd4e, 12) + b;
        e = rol(e, 10);
        b = rol(b + f5(c, d, e) + x[3] + 0xa953fd4e, 13) + a;
        d = rol(d, 10);
        a = rol(a + f5(b, c, d) + x[8] + 0xa953fd4e, 14) + e;
        c = rol(c, 10);
        e = rol(e + f5(a, b, c) + x[11] + 0xa953fd4e, 11) + d;
        b = rol(b, 10);
        d = rol(d + f5(e, a, b) + x[6] + 0xa953fd4e, 8) + c;
        a = rol(a, 10);
        c = rol(c + f5(d, e, a) + x[15] + 0xa953fd4e, 5) + b;
        e = rol(e, 10);
        b = rol(b + f5(c, d, e) + x[13] + 0xa953fd4e, 6) + a;
        d = rol(d, 10);

        // right
        bb = rol(bb + f1(cc, dd, ee) + x[12], 8) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f1(bb, cc, dd) + x[15], 5) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f1(aa, bb, cc) + x[10], 12) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f1(ee, aa, bb) + x[4], 9) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f1(dd, ee, aa) + x[1], 12) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f1(cc, dd, ee) + x[5], 5) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f1(bb, cc, dd) + x[8], 14) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f1(aa, bb, cc) + x[7], 6) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f1(ee, aa, bb) + x[6], 8) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f1(dd, ee, aa) + x[2], 13) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f1(cc, dd, ee) + x[13], 6) + aa;
        dd = rol(dd, 10);
        aa = rol(aa + f1(bb, cc, dd) + x[14], 5) + ee;
        cc = rol(cc, 10);
        ee = rol(ee + f1(aa, bb, cc) + x[0], 15) + dd;
        bb = rol(bb, 10);
        dd = rol(dd + f1(ee, aa, bb) + x[3], 13) + cc;
        aa = rol(aa, 10);
        cc = rol(cc + f1(dd, ee, aa) + x[9], 11) + bb;
        ee = rol(ee, 10);
        bb = rol(bb + f1(cc, dd, ee) + x[11], 11) + aa;
        dd = rol(dd, 10);

        dd += c + h1;
        h1 = h2 + d + ee;
        h2 = h3 + e + aa;
        h3 = h4 + a + bb;
        h4 = h0 + b + cc;
        h0 = dd;

        //
        // reset the offset and clean out the word buffer.
        //
        xOff = 0;
        for (int i = 0; i != x.length; i++) {
            x[i] = 0;
        }
    }

    private void processWord(byte[] in, int inOff) {
        x[xOff++] = readLE32(in, inOff);
        if (xOff == 16) {
            processBlock();
        }
    }


    private void update(byte in) {
        xBuf[xBufOff++] = in;

        if (xBufOff == xBuf.length) {
            processWord(xBuf, 0);
            xBufOff = 0;
        }

        byteCount++;
    }

    public byte[] digest(byte[] in) {
        int i = 0;
        int inOff = 0;
        int len = in.length;

        // 处理整个字节
        int limit = ((len - i) & ~3) + i;
        for (; i < limit; i += 4) {
            processWord(in, inOff + i);
        }

        // 处理剩余部分
        while (i < len) {
            xBuf[xBufOff++] = in[inOff + i++];
        }

        byteCount += len;

        long bitLength = (byteCount << 3);

        //
        // 加入 pad bytes.
        //
        update((byte) 128);

        while (xBufOff != 0) {
            update((byte) 0);
        }

        if (xOff > 14) {
            processBlock();
        }

        // 最后再来一次
        x[14] = (int) (bitLength & 0xffffffff);
        x[15] = (int) (bitLength >>> 32);
        processBlock();


        final byte[] output = new byte[20];

        writeLE32(output, h0, 0);
        writeLE32(output, h1, 4);
        writeLE32(output, h2, 8);
        writeLE32(output, h3, 12);
        writeLE32(output, h4, 16);

        return output;
    }


    private void reset() {
        byteCount = 0;

        xBufOff = 0;
        for (int i = 0; i < xBuf.length; i++) {
            xBuf[i] = 0;
        }

        h0 = 0x67452301;
        h1 = 0xefcdab89;
        h2 = 0x98badcfe;
        h3 = 0x10325476;
        h4 = 0xc3d2e1f0;

        xOff = 0;

        for (int i = 0; i != x.length; i++) {
            x[i] = 0;
        }
    }

    private static int readLE32(final byte[] v, final int i) {
        return v[i] & 0xff |
                ((v[i + 1] & 0xff) << 8) |
                ((v[i + 2] & 0xff) << 16) |
                ((v[i + 3] & 0xff) << 24);
    }

    private static void writeLE32(byte[] v, int n, int i) {
        v[i] = (byte) n;
        v[i + 1] = (byte) ((n >>> 8) & 0xff);
        v[i + 2] = (byte) ((n >>> 16) & 0xff);
        v[i + 3] = (byte) ((n >>> 24) & 0xff);
    }


    private static int rol(int v, int c) {
        return (v << c) | (v >>> (32 - c));
    }

    // F
    /*
     * rounds 0-15
     */
    private static int f1(int x, int y, int z) {
        return x ^ y ^ z;
    }

    /*
     * rounds 16-31
     */
    private static int f2(int x, int y, int z) {
        return (x & y) | (~x & z);
    }

    /*
     * rounds 32-47
     */
    private static int f3(int x, int y, int z) {
        return (x | ~y) ^ z;
    }

    /*
     * rounds 48-63
     */
    private static int f4(int x, int y, int z) {
        return (x & z) | (y & ~z);
    }

    /*
     * rounds 64-79
     */
    private static int f5(int x, int y, int z) {
        return x ^ (y | ~z);
    }
}
