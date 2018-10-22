package one.realme.app

import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.versionOption
import one.realme.app.cmd.AddressCmd
import one.realme.app.cmd.RoCmd
import org.slf4j.LoggerFactory

object App {
    val log = LoggerFactory.getLogger(App.javaClass)!!

    @JvmStatic
    fun main(args: Array<String>) {
        // parse args
        RoCmd()
                .versionOption("1", message = { "Realme.One version $it" })
                .subcommands(AddressCmd())
                .main(args)
    }
}