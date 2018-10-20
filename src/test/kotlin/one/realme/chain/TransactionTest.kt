package one.realme.chain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TransactionTest {

    @Test
    fun testTx() {
        val from = Address("16dxjdBKM9o3pHv9pMo8gGh4zzthapfhCw")
        val to = Address("1NHA43BPGU5CFjGLzht5KDyZPeccdGUPch")
        val amount = Coin.ONE * 10 // 10 coins
        val tx1 = Transaction.create(from, to, amount)
        val tx1Hash = tx1.hash()
        println(tx1Hash.toString())
        assertEquals(32, tx1.hash().toBytes().size)
    }
}