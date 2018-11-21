package one.realme.krot.service.chain

import com.google.common.util.concurrent.AbstractService
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

    override fun doStop() {
        notifyStopped()
    }

    override fun doStart() {
        createGenesisBlock()
        notifyStarted()
    }

    private fun createGenesisBlock() {
        chain = BlockChain(Block(
                height = 0,
                timestamp = UnixTime.fromSeconds(1540166400)
        ))
    }
}