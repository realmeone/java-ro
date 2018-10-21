package one.realme.chain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransactionTest {

    @Test
    fun testTxCoinbase() {
        val to = Address("1NHA43BPGU5CFjGLzht5KDyZPeccdGUPch")
        val txCb = Transaction.coinbase(to)
        println(txCb)
        println(txCb.hash())
        assertEquals(32, txCb.hash().toBytes().size)
    }

    @Test
    fun testTx() {
        val from = Address("16dxjdBKM9o3pHv9pMo8gGh4zzthapfhCw")
        val to = Address("1NHA43BPGU5CFjGLzht5KDyZPeccdGUPch")
        val amount = Coin.ONE * 10 // 10 coins
        val tx1 = Transaction(from, to, amount)
        val tx1Hash = tx1.hash()
        println(tx1Hash.toString())
        assertEquals(32, tx1.hash().toBytes().size)
    }

    @Test
    fun testTxWithPayload() {
        val from = Address("16dxjdBKM9o3pHv9pMo8gGh4zzthapfhCw")
        val to = Address("1NHA43BPGU5CFjGLzht5KDyZPeccdGUPch")
        val amount = Coin.ONE * 10 // 10 coins
        val tx1 = Transaction(from, to, amount, "The Times 03/Jan/2009 Chancellor on brink of second bailout for banks".toByteArray())
        val tx1Hash = tx1.hash()
        println(tx1Hash.toString())
        assertEquals(32, tx1.hash().toBytes().size)
    }

    @Test
    fun testTxWithPayloadScript() {
        val from = Address("16dxjdBKM9o3pHv9pMo8gGh4zzthapfhCw")
        val to = Address("1NHA43BPGU5CFjGLzht5KDyZPeccdGUPch")
        val amount = Coin.ONE * 10 // 10 coins
        val tx1 = Transaction(from, to, amount, "OP_DUP OP_HASH160 <pubKeyHash> OP_EQUALVERIFY OP_CHECKSIG".toByteArray())
        val tx1Hash = tx1.hash()
        println(tx1Hash.toString())
        assertEquals(32, tx1.hash().toBytes().size)
    }
}