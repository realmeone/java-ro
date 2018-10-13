package one.realme.common

import one.realme.crypto.encoding.Hex

fun ByteArray.sha256() = Digests.sha256(this)
fun ByteArray.toHexString() = Hex.encode(this)!!
fun ByteArray.ripemd160() = Digests.ripemd160(this)
fun ByteArray.sha256Twice() = Digests.sha256Twice(this)