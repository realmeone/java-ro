package one.realme.krot.program


import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.versionOption
import one.realme.krot.common.Version
import one.realme.krot.program.cmd.AddressCmd
import one.realme.krot.program.cmd.address.CreateCmd
import one.realme.krot.program.cmd.address.ListCmd

object Main {
    @JvmStatic
    fun main(args: Array<String>) {

        // aplication command
        object : CliktCommand(name = "krot", invokeWithoutSubcommand = true) {
            override fun run() {
                if (context.invokedSubcommand == null) { // no subcommand run this
                    Krot.exec()
                }
            }
        }.versionOption(Version.CURRENT.toString(), message = { "Realme.One version $it" })
                .subcommands(
                        AddressCmd.subcommands(
                                CreateCmd,
                                ListCmd
                        )
                ).main(args)
    }
}