package one.realme.krot.program

import com.google.common.util.concurrent.AbstractService
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.common.primitive.Block
import one.realme.krot.common.primitive.Hash
import one.realme.krot.module.chain.BlockChain

class AppInitService(private val context: AppContext) : AbstractService() {

    override fun doStart() {
        // create genesis
        val genesis = createGenesisBlock()
        context.chain = BlockChain(genesis)
        context.config = AppEnvironment()
        context.config.load("testnet.conf")
    }

    private fun createGenesisBlock(): Block =
            Block(
                    version = context.config.genesis.version,
                    height = context.config.genesis.height,
                    prevBlockHash = Hash.fromString(context.config.genesis.prevBlockHash),
                    timestamp = UnixTime.fromSeconds(context.config.genesis.timestamp)
            )

    override fun doStop() {

    }
}