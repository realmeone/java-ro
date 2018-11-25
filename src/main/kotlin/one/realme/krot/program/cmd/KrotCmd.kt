package one.realme.krot.program.cmd

import com.github.ajalt.clikt.core.CliktCommand
import one.realme.krot.program.app.FullNode

object KrotCmd : CliktCommand(name = "krot", invokeWithoutSubcommand = true) {
    override fun run() {
        if (context.invokedSubcommand == null) { // no subcommand run this
            FullNode.exec("testnet.conf")
        }
    }
}