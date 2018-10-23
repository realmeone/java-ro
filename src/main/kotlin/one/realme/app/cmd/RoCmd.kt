package one.realme.app.cmd

import com.github.ajalt.clikt.core.CliktCommand
import one.realme.app.Banner
import one.realme.app.Node

/**
 * ro the main entry point into the system if no special subcommand is ran.
 */
object RoCmd : CliktCommand(name = "ro", invokeWithoutSubcommand = true) {
    override fun run() {
        if (context.invokedSubcommand == null) { // no subcommand run this
            Banner.printBanner()
            Node.launch()
        }
    }
}