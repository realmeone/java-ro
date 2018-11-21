package one.realme.krot.service.chain

import one.realme.krot.common.appbase.AbstractService
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.common.primitive.Block
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * chain service (facade)
 */
class ChainService : AbstractService() {
    private val log: Logger = LoggerFactory.getLogger(ChainService::class.java)
    private lateinit var chain: BlockChain

    override fun name(): String = "ChainService"

    override fun initialize() {
        createGenesisBlock()
    }

    override fun startup() {}

    override fun shutdown() {}

    private fun createGenesisBlock() {
        chain = BlockChain(Block(
                height = 0,
                timestamp = UnixTime.fromSeconds(1540166400)
        ))
    }
}