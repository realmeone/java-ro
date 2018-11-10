package one.realme.krot.chain

import org.junit.jupiter.api.Test

class BlockChainTest {

    @Test
    fun testNewChain() {

        val wallet = Wallet.create()

        val chain = BlockChain(Block.genesis())
        val b1 = Block(height = chain.tailBlock.height.inc(),
                prevBlockHash = chain.tailBlock.hash)
        b1.transactions.add(Transaction.coinbase(wallet.address))
        chain.push(b1)

        val b2 = Block(height = chain.tailBlock.height.inc(),
                prevBlockHash = chain.tailBlock.hash)
        b2.transactions.add(Transaction.coinbase(wallet.address))
        chain.push(b2)

        chain.slice(LongRange(1, 2)).forEach {
            println(it)
        }
    }
}