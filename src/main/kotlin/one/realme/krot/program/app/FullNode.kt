package one.realme.krot.program.app

import one.realme.krot.common.base.Application
import one.realme.krot.program.Banner
import one.realme.krot.service.chain.ChainService
import one.realme.krot.service.discover.DiscoverService
import one.realme.krot.service.net.NetService

object FullNode {
    fun exec() {
        Banner.printBanner()
        val app = Application(javaClass, listOf(
                ChainService(),
                NetService(),
                DiscoverService()
        ))
        app.start()
    }
}