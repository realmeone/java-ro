package one.realme.krot.chain

import com.google.common.util.concurrent.AbstractService
import org.slf4j.LoggerFactory

object BlockChainService : AbstractService() {
    private val log = LoggerFactory.getLogger(BlockChainService::class.java)
    lateinit var bc: BlockChain

    override fun doStart() {
        log.info("Load block chain...")
        // init block chain
        bc = BlockChain(Block.genesis())
    }

    override fun doStop() {

    }

}