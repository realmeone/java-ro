package one.realme.krot.crypto.digest

/**
 * ripemd 160 hash
 * <p>
 * Covert from bitcoin source hashBytes.h and hashBytes.cpp
 * <p>
 */
class Ripemd160 {
    private var h0: Int = 0
    private var h1: Int = 0
    private var h2: Int = 0
    private var h3: Int = 0
    private var h4: Int = 0

    private val x = IntArray(16)
    private val xBuf = ByteArray(4)
    private var xOff: Int = 0
    private var xBufOff: Int = 0
    private var byteCount: Long = 0

    init {
        reset()
    }

    private fun reset() {
        byteCount = 0

        xBufOff = 0
        for (i in xBuf.indices) {
            xBuf[i] = 0
        }

        h0 = 0x67452301
        h1 = -0x10325477
        h2 = -0x67452302
        h3 = 0x10325476
        h4 = -0x3c2d1e10

        xOff = 0

        for (i in x.indices) {
            x[i] = 0
        }
    }

    fun digest(data: ByteArray): ByteArray {
        var i = 0
        val inOff = 0
        val len = data.size

        val limit = (len - i and 3.inv()) + i
        while (i < limit) {
            processWord(data, inOff + i)
            i += 4
        }

        while (i < len) {
            xBuf[xBufOff++] = data[inOff + i++]
        }

        byteCount += len.toLong()

        val bitLength = byteCount shl 3

        update(128.toByte())

        while (xBufOff != 0) {
            update(0.toByte())
        }

        if (xOff > 14) {
            processBlock()
        }

        x[14] = (bitLength and -0x1).toInt()
        x[15] = bitLength.ushr(32).toInt()
        processBlock()


        val output = ByteArray(20)

        writeLE32(output, h0, 0)
        writeLE32(output, h1, 4)
        writeLE32(output, h2, 8)
        writeLE32(output, h3, 12)
        writeLE32(output, h4, 16)

        return output
    }


    private fun readLE32(v: ByteArray, i: Int): Int {
        return v[i].toInt() and 0xff or
                (v[i + 1].toInt() and 0xff shl 8) or
                (v[i + 2].toInt() and 0xff shl 16) or
                (v[i + 3].toInt() and 0xff shl 24)
    }

    private fun writeLE32(v: ByteArray, n: Int, i: Int) {
        v[i] = n.toByte()
        v[i + 1] = (n.ushr(8) and 0xff).toByte()
        v[i + 2] = (n.ushr(16) and 0xff).toByte()
        v[i + 3] = (n.ushr(24) and 0xff).toByte()
    }


    private fun rol(v: Int, c: Int): Int {
        return v shl c or v.ushr(32 - c)
    }

    // F
    /*
     * rounds 0-15
     */
    private fun f1(x: Int, y: Int, z: Int): Int {
        return x xor y xor z
    }

    /*
     * rounds 16-31
     */
    private fun f2(x: Int, y: Int, z: Int): Int {
        return x and y or (x.inv() and z)
    }

    /*
     * rounds 32-47
     */
    private fun f3(x: Int, y: Int, z: Int): Int {
        return x or y.inv() xor z
    }

    /*
     * rounds 48-63
     */
    private fun f4(x: Int, y: Int, z: Int): Int {
        return x and z or (y and z.inv())
    }

    /*
     * rounds 64-79
     */
    private fun f5(x: Int, y: Int, z: Int): Int {
        return x xor (y or z.inv())
    }

    private fun processWord(`in`: ByteArray, inOff: Int) {
        x[xOff++] = readLE32(`in`, inOff)
        if (xOff == 16) {
            processBlock()
        }
    }


    private fun update(`in`: Byte) {
        xBuf[xBufOff++] = `in`

        if (xBufOff == xBuf.size) {
            processWord(xBuf, 0)
            xBufOff = 0
        }

        byteCount++
    }


    private fun processBlock() {
        var a: Int
        var aa: Int
        var b: Int
        var bb: Int
        var c: Int
        var cc: Int
        var d: Int
        var dd: Int
        var e: Int
        var ee: Int

        aa = h0
        a = aa
        bb = h1
        b = bb
        cc = h2
        c = cc
        dd = h3
        d = dd
        ee = h4
        e = ee

        //
        // Rounds 1 - 16
        //
        // left
        a = rol(a + f1(b, c, d) + x[0], 11) + e
        c = rol(c, 10)
        e = rol(e + f1(a, b, c) + x[1], 14) + d
        b = rol(b, 10)
        d = rol(d + f1(e, a, b) + x[2], 15) + c
        a = rol(a, 10)
        c = rol(c + f1(d, e, a) + x[3], 12) + b
        e = rol(e, 10)
        b = rol(b + f1(c, d, e) + x[4], 5) + a
        d = rol(d, 10)
        a = rol(a + f1(b, c, d) + x[5], 8) + e
        c = rol(c, 10)
        e = rol(e + f1(a, b, c) + x[6], 7) + d
        b = rol(b, 10)
        d = rol(d + f1(e, a, b) + x[7], 9) + c
        a = rol(a, 10)
        c = rol(c + f1(d, e, a) + x[8], 11) + b
        e = rol(e, 10)
        b = rol(b + f1(c, d, e) + x[9], 13) + a
        d = rol(d, 10)
        a = rol(a + f1(b, c, d) + x[10], 14) + e
        c = rol(c, 10)
        e = rol(e + f1(a, b, c) + x[11], 15) + d
        b = rol(b, 10)
        d = rol(d + f1(e, a, b) + x[12], 6) + c
        a = rol(a, 10)
        c = rol(c + f1(d, e, a) + x[13], 7) + b
        e = rol(e, 10)
        b = rol(b + f1(c, d, e) + x[14], 9) + a
        d = rol(d, 10)
        a = rol(a + f1(b, c, d) + x[15], 8) + e
        c = rol(c, 10)

        // right
        aa = rol(aa + f5(bb, cc, dd) + x[5] + 0x50a28be6, 8) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f5(aa, bb, cc) + x[14] + 0x50a28be6, 9) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f5(ee, aa, bb) + x[7] + 0x50a28be6, 9) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f5(dd, ee, aa) + x[0] + 0x50a28be6, 11) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f5(cc, dd, ee) + x[9] + 0x50a28be6, 13) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f5(bb, cc, dd) + x[2] + 0x50a28be6, 15) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f5(aa, bb, cc) + x[11] + 0x50a28be6, 15) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f5(ee, aa, bb) + x[4] + 0x50a28be6, 5) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f5(dd, ee, aa) + x[13] + 0x50a28be6, 7) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f5(cc, dd, ee) + x[6] + 0x50a28be6, 7) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f5(bb, cc, dd) + x[15] + 0x50a28be6, 8) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f5(aa, bb, cc) + x[8] + 0x50a28be6, 11) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f5(ee, aa, bb) + x[1] + 0x50a28be6, 14) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f5(dd, ee, aa) + x[10] + 0x50a28be6, 14) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f5(cc, dd, ee) + x[3] + 0x50a28be6, 12) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f5(bb, cc, dd) + x[12] + 0x50a28be6, 6) + ee
        cc = rol(cc, 10)

        //
        // Rounds 16-31
        //
        // left
        e = rol(e + f2(a, b, c) + x[7] + 0x5a827999, 7) + d
        b = rol(b, 10)
        d = rol(d + f2(e, a, b) + x[4] + 0x5a827999, 6) + c
        a = rol(a, 10)
        c = rol(c + f2(d, e, a) + x[13] + 0x5a827999, 8) + b
        e = rol(e, 10)
        b = rol(b + f2(c, d, e) + x[1] + 0x5a827999, 13) + a
        d = rol(d, 10)
        a = rol(a + f2(b, c, d) + x[10] + 0x5a827999, 11) + e
        c = rol(c, 10)
        e = rol(e + f2(a, b, c) + x[6] + 0x5a827999, 9) + d
        b = rol(b, 10)
        d = rol(d + f2(e, a, b) + x[15] + 0x5a827999, 7) + c
        a = rol(a, 10)
        c = rol(c + f2(d, e, a) + x[3] + 0x5a827999, 15) + b
        e = rol(e, 10)
        b = rol(b + f2(c, d, e) + x[12] + 0x5a827999, 7) + a
        d = rol(d, 10)
        a = rol(a + f2(b, c, d) + x[0] + 0x5a827999, 12) + e
        c = rol(c, 10)
        e = rol(e + f2(a, b, c) + x[9] + 0x5a827999, 15) + d
        b = rol(b, 10)
        d = rol(d + f2(e, a, b) + x[5] + 0x5a827999, 9) + c
        a = rol(a, 10)
        c = rol(c + f2(d, e, a) + x[2] + 0x5a827999, 11) + b
        e = rol(e, 10)
        b = rol(b + f2(c, d, e) + x[14] + 0x5a827999, 7) + a
        d = rol(d, 10)
        a = rol(a + f2(b, c, d) + x[11] + 0x5a827999, 13) + e
        c = rol(c, 10)
        e = rol(e + f2(a, b, c) + x[8] + 0x5a827999, 12) + d
        b = rol(b, 10)

        // right
        ee = rol(ee + f4(aa, bb, cc) + x[6] + 0x5c4dd124, 9) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f4(ee, aa, bb) + x[11] + 0x5c4dd124, 13) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f4(dd, ee, aa) + x[3] + 0x5c4dd124, 15) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f4(cc, dd, ee) + x[7] + 0x5c4dd124, 7) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f4(bb, cc, dd) + x[0] + 0x5c4dd124, 12) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f4(aa, bb, cc) + x[13] + 0x5c4dd124, 8) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f4(ee, aa, bb) + x[5] + 0x5c4dd124, 9) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f4(dd, ee, aa) + x[10] + 0x5c4dd124, 11) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f4(cc, dd, ee) + x[14] + 0x5c4dd124, 7) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f4(bb, cc, dd) + x[15] + 0x5c4dd124, 7) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f4(aa, bb, cc) + x[8] + 0x5c4dd124, 12) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f4(ee, aa, bb) + x[12] + 0x5c4dd124, 7) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f4(dd, ee, aa) + x[4] + 0x5c4dd124, 6) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f4(cc, dd, ee) + x[9] + 0x5c4dd124, 15) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f4(bb, cc, dd) + x[1] + 0x5c4dd124, 13) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f4(aa, bb, cc) + x[2] + 0x5c4dd124, 11) + dd
        bb = rol(bb, 10)

        //
        // Rounds 32-47
        //
        // left
        d = rol(d + f3(e, a, b) + x[3] + 0x6ed9eba1, 11) + c
        a = rol(a, 10)
        c = rol(c + f3(d, e, a) + x[10] + 0x6ed9eba1, 13) + b
        e = rol(e, 10)
        b = rol(b + f3(c, d, e) + x[14] + 0x6ed9eba1, 6) + a
        d = rol(d, 10)
        a = rol(a + f3(b, c, d) + x[4] + 0x6ed9eba1, 7) + e
        c = rol(c, 10)
        e = rol(e + f3(a, b, c) + x[9] + 0x6ed9eba1, 14) + d
        b = rol(b, 10)
        d = rol(d + f3(e, a, b) + x[15] + 0x6ed9eba1, 9) + c
        a = rol(a, 10)
        c = rol(c + f3(d, e, a) + x[8] + 0x6ed9eba1, 13) + b
        e = rol(e, 10)
        b = rol(b + f3(c, d, e) + x[1] + 0x6ed9eba1, 15) + a
        d = rol(d, 10)
        a = rol(a + f3(b, c, d) + x[2] + 0x6ed9eba1, 14) + e
        c = rol(c, 10)
        e = rol(e + f3(a, b, c) + x[7] + 0x6ed9eba1, 8) + d
        b = rol(b, 10)
        d = rol(d + f3(e, a, b) + x[0] + 0x6ed9eba1, 13) + c
        a = rol(a, 10)
        c = rol(c + f3(d, e, a) + x[6] + 0x6ed9eba1, 6) + b
        e = rol(e, 10)
        b = rol(b + f3(c, d, e) + x[13] + 0x6ed9eba1, 5) + a
        d = rol(d, 10)
        a = rol(a + f3(b, c, d) + x[11] + 0x6ed9eba1, 12) + e
        c = rol(c, 10)
        e = rol(e + f3(a, b, c) + x[5] + 0x6ed9eba1, 7) + d
        b = rol(b, 10)
        d = rol(d + f3(e, a, b) + x[12] + 0x6ed9eba1, 5) + c
        a = rol(a, 10)

        // right
        dd = rol(dd + f3(ee, aa, bb) + x[15] + 0x6d703ef3, 9) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f3(dd, ee, aa) + x[5] + 0x6d703ef3, 7) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f3(cc, dd, ee) + x[1] + 0x6d703ef3, 15) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f3(bb, cc, dd) + x[3] + 0x6d703ef3, 11) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f3(aa, bb, cc) + x[7] + 0x6d703ef3, 8) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f3(ee, aa, bb) + x[14] + 0x6d703ef3, 6) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f3(dd, ee, aa) + x[6] + 0x6d703ef3, 6) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f3(cc, dd, ee) + x[9] + 0x6d703ef3, 14) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f3(bb, cc, dd) + x[11] + 0x6d703ef3, 12) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f3(aa, bb, cc) + x[8] + 0x6d703ef3, 13) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f3(ee, aa, bb) + x[12] + 0x6d703ef3, 5) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f3(dd, ee, aa) + x[2] + 0x6d703ef3, 14) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f3(cc, dd, ee) + x[10] + 0x6d703ef3, 13) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f3(bb, cc, dd) + x[0] + 0x6d703ef3, 13) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f3(aa, bb, cc) + x[4] + 0x6d703ef3, 7) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f3(ee, aa, bb) + x[13] + 0x6d703ef3, 5) + cc
        aa = rol(aa, 10)

        //
        // Rounds 48-63
        //
        // left
        c = rol(c + f4(d, e, a) + x[1] + -0x70e44324, 11) + b
        e = rol(e, 10)
        b = rol(b + f4(c, d, e) + x[9] + -0x70e44324, 12) + a
        d = rol(d, 10)
        a = rol(a + f4(b, c, d) + x[11] + -0x70e44324, 14) + e
        c = rol(c, 10)
        e = rol(e + f4(a, b, c) + x[10] + -0x70e44324, 15) + d
        b = rol(b, 10)
        d = rol(d + f4(e, a, b) + x[0] + -0x70e44324, 14) + c
        a = rol(a, 10)
        c = rol(c + f4(d, e, a) + x[8] + -0x70e44324, 15) + b
        e = rol(e, 10)
        b = rol(b + f4(c, d, e) + x[12] + -0x70e44324, 9) + a
        d = rol(d, 10)
        a = rol(a + f4(b, c, d) + x[4] + -0x70e44324, 8) + e
        c = rol(c, 10)
        e = rol(e + f4(a, b, c) + x[13] + -0x70e44324, 9) + d
        b = rol(b, 10)
        d = rol(d + f4(e, a, b) + x[3] + -0x70e44324, 14) + c
        a = rol(a, 10)
        c = rol(c + f4(d, e, a) + x[7] + -0x70e44324, 5) + b
        e = rol(e, 10)
        b = rol(b + f4(c, d, e) + x[15] + -0x70e44324, 6) + a
        d = rol(d, 10)
        a = rol(a + f4(b, c, d) + x[14] + -0x70e44324, 8) + e
        c = rol(c, 10)
        e = rol(e + f4(a, b, c) + x[5] + -0x70e44324, 6) + d
        b = rol(b, 10)
        d = rol(d + f4(e, a, b) + x[6] + -0x70e44324, 5) + c
        a = rol(a, 10)
        c = rol(c + f4(d, e, a) + x[2] + -0x70e44324, 12) + b
        e = rol(e, 10)

        // right
        cc = rol(cc + f2(dd, ee, aa) + x[8] + 0x7a6d76e9, 15) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f2(cc, dd, ee) + x[6] + 0x7a6d76e9, 5) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f2(bb, cc, dd) + x[4] + 0x7a6d76e9, 8) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f2(aa, bb, cc) + x[1] + 0x7a6d76e9, 11) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f2(ee, aa, bb) + x[3] + 0x7a6d76e9, 14) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f2(dd, ee, aa) + x[11] + 0x7a6d76e9, 14) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f2(cc, dd, ee) + x[15] + 0x7a6d76e9, 6) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f2(bb, cc, dd) + x[0] + 0x7a6d76e9, 14) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f2(aa, bb, cc) + x[5] + 0x7a6d76e9, 6) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f2(ee, aa, bb) + x[12] + 0x7a6d76e9, 9) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f2(dd, ee, aa) + x[2] + 0x7a6d76e9, 12) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f2(cc, dd, ee) + x[13] + 0x7a6d76e9, 9) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f2(bb, cc, dd) + x[9] + 0x7a6d76e9, 12) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f2(aa, bb, cc) + x[7] + 0x7a6d76e9, 5) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f2(ee, aa, bb) + x[10] + 0x7a6d76e9, 15) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f2(dd, ee, aa) + x[14] + 0x7a6d76e9, 8) + bb
        ee = rol(ee, 10)

        //
        // Rounds 64-79
        //
        // left
        b = rol(b + f5(c, d, e) + x[4] + -0x56ac02b2, 9) + a
        d = rol(d, 10)
        a = rol(a + f5(b, c, d) + x[0] + -0x56ac02b2, 15) + e
        c = rol(c, 10)
        e = rol(e + f5(a, b, c) + x[5] + -0x56ac02b2, 5) + d
        b = rol(b, 10)
        d = rol(d + f5(e, a, b) + x[9] + -0x56ac02b2, 11) + c
        a = rol(a, 10)
        c = rol(c + f5(d, e, a) + x[7] + -0x56ac02b2, 6) + b
        e = rol(e, 10)
        b = rol(b + f5(c, d, e) + x[12] + -0x56ac02b2, 8) + a
        d = rol(d, 10)
        a = rol(a + f5(b, c, d) + x[2] + -0x56ac02b2, 13) + e
        c = rol(c, 10)
        e = rol(e + f5(a, b, c) + x[10] + -0x56ac02b2, 12) + d
        b = rol(b, 10)
        d = rol(d + f5(e, a, b) + x[14] + -0x56ac02b2, 5) + c
        a = rol(a, 10)
        c = rol(c + f5(d, e, a) + x[1] + -0x56ac02b2, 12) + b
        e = rol(e, 10)
        b = rol(b + f5(c, d, e) + x[3] + -0x56ac02b2, 13) + a
        d = rol(d, 10)
        a = rol(a + f5(b, c, d) + x[8] + -0x56ac02b2, 14) + e
        c = rol(c, 10)
        e = rol(e + f5(a, b, c) + x[11] + -0x56ac02b2, 11) + d
        b = rol(b, 10)
        d = rol(d + f5(e, a, b) + x[6] + -0x56ac02b2, 8) + c
        a = rol(a, 10)
        c = rol(c + f5(d, e, a) + x[15] + -0x56ac02b2, 5) + b
        e = rol(e, 10)
        b = rol(b + f5(c, d, e) + x[13] + -0x56ac02b2, 6) + a
        d = rol(d, 10)

        // right
        bb = rol(bb + f1(cc, dd, ee) + x[12], 8) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f1(bb, cc, dd) + x[15], 5) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f1(aa, bb, cc) + x[10], 12) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f1(ee, aa, bb) + x[4], 9) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f1(dd, ee, aa) + x[1], 12) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f1(cc, dd, ee) + x[5], 5) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f1(bb, cc, dd) + x[8], 14) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f1(aa, bb, cc) + x[7], 6) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f1(ee, aa, bb) + x[6], 8) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f1(dd, ee, aa) + x[2], 13) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f1(cc, dd, ee) + x[13], 6) + aa
        dd = rol(dd, 10)
        aa = rol(aa + f1(bb, cc, dd) + x[14], 5) + ee
        cc = rol(cc, 10)
        ee = rol(ee + f1(aa, bb, cc) + x[0], 15) + dd
        bb = rol(bb, 10)
        dd = rol(dd + f1(ee, aa, bb) + x[3], 13) + cc
        aa = rol(aa, 10)
        cc = rol(cc + f1(dd, ee, aa) + x[9], 11) + bb
        ee = rol(ee, 10)
        bb = rol(bb + f1(cc, dd, ee) + x[11], 11) + aa
        dd = rol(dd, 10)

        dd += c + h1
        h1 = h2 + d + ee
        h2 = h3 + e + aa
        h3 = h4 + a + bb
        h4 = h0 + b + cc
        h0 = dd

        //
        // reset the offset and clean out the word buffer.
        //
        xOff = 0
        for (i in x.indices) {
            x[i] = 0
        }
    }
}