package one.realme.krot.program.app

import one.realme.krot.common.base.Application
import one.realme.krot.program.Banner
import one.realme.krot.service.chain.ChainService
import one.realme.krot.service.discover.DiscoverService
import one.realme.krot.service.net.server.NetService
import org.slf4j.LoggerFactory

object FullNode {
    private val logger = LoggerFactory.getLogger(FullNode.javaClass)
    private val name = FullNode.javaClass.simpleName

    fun exec(conf: String) {
        Banner.printBanner()
        val app = Application(
                name,
                conf,
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