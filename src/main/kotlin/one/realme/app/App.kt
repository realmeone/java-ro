package one.realme.app

import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.versionOption
import one.realme.app.cmd.AddressCmd
import one.realme.app.cmd.RoCmd
import one.realme.app.cmd.address.CreateCmd
import one.realme.app.cmd.address.ListCmd

object App {

    @JvmStatic
    fun main(args: Array<String>) {
        // parse args
        RoCmd.versionOption("1", message = { "Realme.One version $it" })
                .subcommands(
                        AddressCmd.subcommands(
                                CreateCmd,
                                ListCmd
                        )
                )
                .main(args)
    }
}