package one.realme.krot.crypto

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Sha256Test {
    @Test
    fun twiceSha256() {
        val hello = "hello"
        val sha256Hello = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
        val sha256x2Hello = "9595c9df90075148eb06860365df33584b75bff782a510c6cd4883a419833d50"
        assertEquals(sha256Hello, hello.toByteArray().sha256().toHexString())
        assertEquals(sha256x2Hello, hello.toByteArray().sha256Twice().toHexString())
    }

}