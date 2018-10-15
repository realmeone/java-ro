package one.realme.crypto.digest

import one.realme.crypto.sha256
import one.realme.crypto.sha256Twice
import one.realme.crypto.toHexString
import org.junit.Assert
import org.junit.Test

class Sha256Test {
    @Test
    fun twiceSha256() {
        val hello = "hello"
        val sha256Hello = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
        val sha256x2Hello = "9595c9df90075148eb06860365df33584b75bff782a510c6cd4883a419833d50"
        Assert.assertEquals(sha256Hello, hello.toByteArray().sha256().toHexString())
        Assert.assertEquals(sha256x2Hello, hello.toByteArray().sha256Twice().toHexString())
    }

}