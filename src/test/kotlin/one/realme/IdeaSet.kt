package one.realme


import one.realme.common.ripemd160
import one.realme.common.sha256
import one.realme.common.sha256Twice
import one.realme.common.toHexString
import one.realme.crypto.ECSecp256k1
import one.realme.crypto.encoding.Hex
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.security.KeyFactory
import java.security.Signature
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec


class IdeaSet {

    @Test
    fun twiceSha256() {
        val hello = "hello"
        val sha256Hello = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
        val sha256x2Hello = "9595c9df90075148eb06860365df33584b75bff782a510c6cd4883a419833d50"
        assertEquals(sha256Hello, hello.toByteArray().sha256().toHexString())
        assertEquals(sha256x2Hello, hello.toByteArray().sha256Twice().toHexString())
    }

    @Test
    fun addressPreview() {
        val hello = "hello"
        val sha256Hello = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
        val ripemd160Sha256Hello = "b6a9c8c230722b7c748331a8b450f05566dc7d0f"
        assertEquals(sha256Hello, hello.toByteArray().sha256().toHexString())
        assertEquals(ripemd160Sha256Hello, hello.toByteArray().sha256().ripemd160().toHexString())
    }

    @Test
    fun secp256k1() {
        val data = "CF80CD8AED482D5D1527D7DC72FCEFF84E6326592848447D2DC0B0E87DFC9A90".toLowerCase() //sha256hash of "testing"
        val sig = "3044022079BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F817980220294F14E883B3F525B5367756C2A11EF6CF84B730B36C17CB0C56F0AAB2C98589".toLowerCase()
        val pri = "67E56582298859DDAE725F972992A07C6C4FB9F62A8FFF58CE3CA926A1063530".toLowerCase()
        val pub = "040A629506E1B65CD9D2E0BA9C75DF9C4FED0DB16DC9625ED14397F0AFC836FAE595DC53F8B0EFE61E703075BD9B143BAC75EC0E19F82A2208CAEB32BE53414C40".toLowerCase()
        assertEquals("testing".toByteArray().sha256().toHexString().toLowerCase(), data)
        println(data.length)

        // test sha256hash testing
        println(ECSecp256k1.newKeyPair())

        // java pubkey encoded byte to hex will print "3056301006072a8648ce3d020106052b8104000a03420004" + point.x + point.y
        // bitcoin pubkey byte to hex will print "04" + point.x + point.y
        // public_key_256_head 3056301006072a8648ce3d020106052b8104000a03420004
        val p256Head = "3056301006072a8648ce3d020106052b8104000a03420004"
        val pri256Head = "303e020100301006072a8648ce3d020106052b8104000a042730250201010420"
        val rPub = pub.removePrefix("04")
        val x = rPub.substring(0, 64)
        val y = rPub.substring(64)
        val keyFactory = KeyFactory.getInstance("EC")
        println(p256Head + x + y)

        val pubKey = keyFactory.generatePublic(X509EncodedKeySpec(Hex.decode(p256Head + x + y))) as ECPublicKey
        val priKey = keyFactory.generatePrivate(PKCS8EncodedKeySpec(Hex.decode(pri256Head + pri))) as ECPrivateKey

        println("--- key params")
        println(pubKey.encoded.toHexString())

        println("---- data")
        println(data)
        println("testing".toByteArray().sha256().toHexString())

//        if you are already doing the digest yourself. If you don't specify NONEwithECDSA then the implementation will hash a second time and calculate the wrong result.
        val signature = Signature.getInstance("NONEwithECDSA")

        println("--- sign")
        signature.initSign(priKey)
        signature.update(Hex.decode(data))
        val signedData = signature.sign()
        println(Hex.encode(signedData))
        println(sig)

        signature.initVerify(pubKey)
        signature.update(Hex.decode(data))
        assertTrue(signature.verify(Hex.decode(sig)))
    }
}