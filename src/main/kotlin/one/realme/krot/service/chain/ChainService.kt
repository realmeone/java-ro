package one.realme.krot.service.chain

import one.realme.krot.common.base.Application
import one.realme.krot.common.base.BaseService
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.common.primitive.Block
import one.realme.krot.common.primitive.Hash
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * chain service (facade)
 */
class ChainService : BaseService() {
    internal class Configuration {
        var genesisVersion = 1
        var genesisHeight = 0L
        var genesisPrevBlockHash = "00000000000000000000000000000000"
        var genesisTimestamp = 1540166400
    }

    private val log: Logger = LoggerFactory.getLogger(ChainService::class.java)
    private val configuration = Configuration()
    private lateinit var chain: BlockChain

    override fun initialize(app: Application) {
        with(configuration) {
            with(app.config) {
                getIntOrNull("genesis.version")?.let { genesisVersion = it }
                getIntOrNull("genesis.version")?.let { genesisVersion = it }
                getLongOrNull("genesis.height")?.let { genesisHeight = it }
                getStringOrNull("genesis.prevBlockHash")?.let { genesisPrevBlockHash = it }
                getIntOrNull("genesis.timestamp")?.let { genesisTimestamp = it }
            }
        }

    }

    override fun start() {
        createGenesisBlock()
    }

    override fun stop() {
    }

    private fun createGenesisBlock() {
        chain = BlockChain(Block(
                configuration.genesisVersion,
                configuration.genesisHeight,
                Hash.fromString(configuration.genesisPrevBlockHash),
                UnixTime.fromSeconds(1540166400)
        ))
    }
}