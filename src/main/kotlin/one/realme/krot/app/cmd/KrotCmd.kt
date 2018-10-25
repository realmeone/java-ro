package one.realme.krot.app.cmd

import com.github.ajalt.clikt.core.CliktCommand
import one.realme.krot.app.Banner
import one.realme.krot.app.Krot

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