@file:kotlin.jvm.JvmName("TimingKt")
package one.realme.krot.common

/**
 * Executes the given [block] and returns elapsed time in seconds.
 */
inline fun measureTimeSeconds(block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    block()
    return ((System.currentTimeMillis() - start) / 1000.0).toLong()
}