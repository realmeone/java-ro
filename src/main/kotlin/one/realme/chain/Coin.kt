package one.realme.chain

/**
 * todo read conifg for custom coin
 * decimal?
 */
class Coin private constructor(private val value: Long) : Comparable<Coin> {

    operator fun minus(that: Coin): Coin = Coin(this.value - that.value)
    operator fun plus(that: Coin): Coin = Coin(this.value + that.value)


    fun toLong(): Long {
        return this.value
    }

    override fun compareTo(other: Coin): Int {
        return this.value.compareTo(other.value)
    }

    companion object {
        const val DECIMAL = 8

        val ZERO = Coin(0)
        val SATOSHI = Coin(1)
        val COIN = Coin(10_000_000)
        val COIN_BASE_REWARD = Coin(50)
        val COIN_MAX = Coin(1_000_000_000_000_000) // 10 ** 8 ** 8
        val NEGATIVE_SATOSHI = Coin(-1)
    }
}