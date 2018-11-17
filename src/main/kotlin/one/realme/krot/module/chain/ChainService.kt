package one.realme.krot.module.chain

import com.google.common.util.concurrent.AbstractService
import one.realme.krot.common.db.RocksDatabase
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.common.primitive.Block
import one.realme.krot.common.primitive.Transaction
import one.realme.krot.program.ApplicationContext
import java.util.*

class ChainService(val ctx: ApplicationContext) : AbstractService() {
    private val txPool = Vector<Transaction>()
    private val blockDb = BlockDb(RocksDatabase("data/blocks"))
    lateinit var chain: BlockChain

    override fun doStop() {

    }

    override fun doStart() {
        createGenesisBlock()
    }

    private fun createGenesisBlock() {
        chain = BlockChain(Block(
                height = 0,
                timestamp = UnixTime.fromSeconds(1540166400)
        ))
    }
}