package one.realme.krot.program.cmd.address

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import one.realme.krot.common.primitive.Address
import one.realme.krot.common.crypto.Secp256k1

/**
 * ro address create
 */
object CreateCmd : CliktCommand(name = "create") {
    private val number: Int by option("-n", "--number", help = "how many new address do you want?").int().default(1)
    override fun run() {
        for (i in 1..number) {
            val keys = Secp256k1.newKeyPair()
            println("New address created.")
            println("Address: {${Address(keys.second, 0)}}")
            println("Private Key: {${keys.first}}")
            println()
        }
    }

}