package one.realme.krot.program.cmd.node

import com.github.ajalt.clikt.core.CliktCommand
import one.realme.krot.common.base.Application
import one.realme.krot.program.Banner
import one.realme.krot.program.app.FullNode
import one.realme.krot.service.chain.ChainService
import one.realme.krot.service.discover.DiscoverService
import one.realme.krot.service.net.NetService

object NodeCmd : CliktCommand(name = "node", invokeWithoutSubcommand = true) {

    override fun run() {
        if (context.invokedSubcommand == null) { // no subcommand run this
            FullNode.exec()
        }
    }
}
