@file:kotlin.jvm.JvmName("BytesKt")

package one.realme.krot.common

import com.google.common.primitives.Ints
import com.google.common.primitives.Longs

fun Int.toBytes() = Ints.toByteArray(this)
fun Long.toBytes() = Longs.toByteArray(this)