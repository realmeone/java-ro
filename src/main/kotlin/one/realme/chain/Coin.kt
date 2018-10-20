package one.realme.chain

import one.realme.common.toBytes
import one.realme.common.toBytesLE

/**
 * future: read conifg for custom coin
 * decimal?
 */
class Coin private constructor(private val value: Long) : Comparable<Coin> {

    operator fun minus(that: Coin): Coin = Coin(this.value - that.value)
    operator fun plus(that: Coin): Coin = Coin(this.value + that.value)
    operator fun times(factor: Int): Coin = Coin(this.value * factor)


    fun toLong(): Long = this.value
    fun toBytes(): ByteArray = this.value.toBytes()
    fun toBytesLE(): ByteArray = this.value.toBytesLE()

    override fun compareTo(other: Coin): Int {
        return this.value.compareTo(other.value)
    }

    companion object {
        const val DECIMAL = 8

        val ZERO = Coin(0)
        val SATOSHI = Coin(1)
        val ONE = Coin(10_000_000)
        val BASE_REWARD = Coin(50)
        val MAX = Coin(1_000_000_000_000_000) // 10 ** 8 ** 8
        val NEGATIVE_SATOSHI = Coin(-1)
    }
}