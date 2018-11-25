package one.realme.krot.program.cmd.node

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import one.realme.krot.program.app.FullNode

object NodeCmd : CliktCommand(name = "node", invokeWithoutSubcommand = true) {
    private val conf: String by option("--conf", help = "configuration file path").default("testnet.conf")

    override fun run() {
        if (context.invokedSubcommand == null) { // no subcommand run this
            FullNode.exec(conf)
        }
    }
}
