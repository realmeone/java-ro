package one.realme.krot.program.app

import one.realme.krot.common.base.Application
import one.realme.krot.program.Banner
import one.realme.krot.service.chain.ChainService
import one.realme.krot.service.discover.DiscoverService
import one.realme.krot.service.net.NetService
import org.slf4j.LoggerFactory

object FullNode {
    private val logger = LoggerFactory.getLogger(FullNode.javaClass)
    private val name = FullNode.javaClass.simpleName
    private const val confPath = "testnet.conf"

    fun exec() {
        Banner.printBanner()
        val app = Application(
                name,
                confPath,
                listOf(
                        ChainService(),
                        NetService(),
                        DiscoverService()
                ),
                logger
        )
        app.start()
    }
}