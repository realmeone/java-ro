package one.realme.chain

import one.realme.crypto.secp256k1.ECSecp256k1
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 *
 */
class AddressTest {

    @Test
    fun testCreate() {
        val keyPair = ECSecp256k1.newKeyPair()
        val pubKey = keyPair.second
        val addr = Address(pubKey, 0)
        println(addr)
        assertTrue(addr.isValid())
    }


    @Test
    fun testVerify() {
        val cases = listOf(
                // mainnet
                listOf("17VZNX1SN5NtKa8UQFxwQbFeFc3iqRYhem", "47376c6f537d62177a2c41c4ca9b45829ab99083"), // p2pkh
                listOf("3EktnHQD7RiAE6uzMj2ZifT9YgRrkSgzQX", "8f55563b9a19f321c211e9b9f38cdf686ea07845"), // p2sh

                // testnet
                listOf("mipcBbFg9gMiCh81Kj8tqqdgoZub1ZJRfn", "243f1394f44554f4ce3fd68649c19adc483ce924"), // p2pkh
                listOf("2MzQwSSnBHWHqSAqtTVQ6v47XtaisrJa1Vc", "4e9f39ca4688ff102128ea4ccda34105324305b0") // p2sh
        )

        for (case in cases) {
            val addr = Address(case[0])
            assertTrue(addr.isValid())
            assertEquals(case[1], addr.hash160())
        }
    }


    @Test
    fun testVerifyFake() {
        val cases = listOf(
                "1AGNa15ZQXAZUgFiqJ2i7Z2DPU2J6hW62j",
                "1AGNa15ZQXAZUgFiqJ2i7Z2DPU2J6hW62X",
                "1ANNa15ZQXAZUgFiqJ2i7Z2DPU2J6hW62i",
                "1A Na15ZQXAZUgFiqJ2i7Z2DPU2J6hW62i",
                "BZbvjr",
                "i55j",
                "1AGNa15ZQXAZUgFiqJ2i7Z2DPU2J6hW62",
                "1AGNa15ZQXAZUgFiqJ2i7Z2DPU2J6hW62iz",
                "1AGNa15ZQXAZUgFiqJ2i7Z2DPU2J6hW62izz",
                "1Q1pE5vPGEEMqRcVRMbtBK842Y6Pzo6nJ9",
                "1AGNa15ZQXAZUgFiqJ2i7Z2DPU2J6hW62I"
        )
        for (case in cases) {
            try {
                assertFalse(Address(case).isValid())
            } catch (ignore: IllegalArgumentException) {
                // some address are not a valid base58 string
            }
        }
    }
}