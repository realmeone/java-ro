package one.realme.krot.service.chain

import com.google.common.util.concurrent.AbstractService
import one.realme.krot.common.appbase.Configuration
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.common.primitive.Block
import org.slf4j.LoggerFactory

/**
 * chain service (facade)
 */
class ChainService(config: Configuration) : AbstractService() {
    private val log = LoggerFactory.getLogger(ChainService::class.java)
    private lateinit var chain: BlockChain

    public override fun doStart() {
        createGenesisBlock()
        log.info("Chain service stared.")
        notifyStarted()
    }

    private fun createGenesisBlock() {
        chain = BlockChain(Block(
                height = 0,
                timestamp = UnixTime.fromSeconds(1540166400)
        ))
    }

    override fun doStop() {
        log.info("Chain service stopped.")
        notifyStopped()
    }
}