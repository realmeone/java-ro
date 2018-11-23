package one.realme.krot.program


import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoRunCliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.versionOption
import one.realme.krot.common.Version
import one.realme.krot.program.cmd.KrotCmd
import one.realme.krot.program.cmd.address.AddressCmd
import one.realme.krot.program.cmd.address.CreateCmd
import one.realme.krot.program.cmd.address.ListCmd
import one.realme.krot.program.cmd.node.NodeCmd

object Krot {

    @JvmStatic
    fun main(args: Array<String>) {
        /*
            Command:
            krot
                -- help
                -- ui
                -- fullnode
                -- account --create [n]
                                --list
                -- version
         */
        KrotCmd.versionOption(Version.CURRENT.toString(), message = { "Krot version $it" })
                .subcommands(
                        NodeCmd,
                        AddressCmd.subcommands(
                                CreateCmd,
                                ListCmd
                        )
                ).main(args)
    }
}