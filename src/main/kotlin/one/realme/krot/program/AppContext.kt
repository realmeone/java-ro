package one.realme.krot.program

import one.realme.krot.module.chain.BlockChain

/**
 * Not thread safe
 */
class AppContext {
    lateinit var chain: BlockChain
    lateinit var config: AppEnvironment
}