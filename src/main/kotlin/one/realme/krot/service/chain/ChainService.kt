package one.realme.krot.service.chain

import one.realme.krot.common.base.BaseService
import one.realme.krot.common.base.Application
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.common.primitive.Block
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * chain service (facade)
 */
class ChainService : BaseService() {
    private val log: Logger = LoggerFactory.getLogger(ChainService::class.java)
    private lateinit var chain: BlockChain

    override fun initialize(app: Application) {
    }

    override fun start() {
        createGenesisBlock()
    }

    override fun stop() {
    }

    private fun createGenesisBlock() {
        chain = BlockChain(Block(
                height = 0,
                timestamp = UnixTime.fromSeconds(1540166400)
        ))
    }
}