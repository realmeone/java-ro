package one.realme.crypto.ksecp256k1

import com.google.common.base.Stopwatch
import one.realme.crypto.secp256k1.ECPoint
import java.math.BigInteger
import java.util.concurrent.TimeUnit

/**
 * secp256k1
 * kotlin is slower than java implements
 *
 * kotlin: v1.2.71
 * java: oracle-jvm 1.8
 *
 * total: 100
 * kotlin use: 0.62  seconds
 * java use : 0.598 seconds
 *
 * total: 1000
 * kotlin use: 5.124  seconds
 * java use : 5.04 seconds
 *
 * total: 2000
 * java use: 9.864  seconds
 * kotlin use : 10.562 seconds
 */
@Deprecated("use java implements")
object Secp256k1 {

    @JvmStatic
    fun main(args: Array<String>) {
        val gX = BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16)
        val gY = BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16)
        val order = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16)

        val curve256 = CurveFp.curve256()
        val generator256 = Point(curve256, gX, gY)
        println("---------------------------------------------------")
        val secret = BigInteger("67E56582298859DDAE725F972992A07C6C4FB9F62A8FFF58CE3CA926A1063530", 16)
//        val secret = BigInteger("8F72F6B29E6E225A36B68DFE333C7CE5E55D83249D3D2CD6332671FA445C4DD3", 16)
        println(secret.toString(16))
        println("04 C591A8FF19AC9C4E4E5793673B83123437E975285E7B442F4EE2654DFFCA5E2D 2103ED494718C697AC9AEBCFD19612E224DB46661011863ED2FC54E71861E2A6")
//        println("04 06CCAE7536386DA2C5ADD428B099C7658814CA837F94FADE365D0EC6B1519385 FF83EC5F2C0C8F016A32134589F7B9E97ACBFEFD2EF12A91FA622B38A1449EEB")

        println("---------------------------------------------------")
        val pubkeyPoint = generator256 * secret
        println("---------------------------------------------------")
        println("04 ${pubkeyPoint.x.toString(16).toUpperCase()} ${pubkeyPoint.y.toString(16).toUpperCase()}")

        val total = 1000

        // java version
        val watch = Stopwatch.createStarted()
        val curveFpJ = one.realme.crypto.secp256k1.CurveFp.curve256()
        val bigG = ECPoint(curveFpJ, gX, gY)
        for (i in 0..total) {
            bigG.multiply(secret)
        }
        watch.stop()
        println("java use:  ${watch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")

        // kotlin version
        watch.reset()
        watch.start()
        for (i in 0..total) {
            generator256 * secret
        }
        watch.stop()
        println("kotlin use:  ${watch.elapsed(TimeUnit.MILLISECONDS) / 1000.0} seconds")

    }
}