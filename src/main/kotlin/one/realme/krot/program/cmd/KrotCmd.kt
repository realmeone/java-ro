package one.realme.krot.program.cmd

import com.github.ajalt.clikt.core.CliktCommand
import one.realme.krot.program.Banner
import one.realme.krot.program.Krot

/**
 * ro the main entry point into the system if no special subcommand is ran.
 */
object KrotCmd : CliktCommand(name = "krot", invokeWithoutSubcommand = true) {
    override fun run() {
        if (context.invokedSubcommand == null) { // no subcommand run this
            Banner.printBanner()
            Krot.launch()
        }
    }
}