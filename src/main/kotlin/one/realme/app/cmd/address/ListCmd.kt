package one.realme.app.cmd.address

import com.github.ajalt.clikt.core.CliktCommand

/**
 * ro address list
 */
object ListCmd : CliktCommand(name = "list") {
    override fun run() {
        println("list addresses")
    }

}