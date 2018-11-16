package one.realme.krot

import one.realme.krot.common.Version
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.common.primitive.Block
import one.realme.krot.common.primitive.Hash

fun Block.Companion.genesis() = Block(
        Version.CURRENT,
        0,
        Hash.empty(),
        UnixTime.fromSeconds(1540166400)
)