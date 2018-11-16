package one.realme.krot.common.primitive

import one.realme.krot.common.lang.UnixTime

data class BlockHeader(
        val version: Int,
        val prevBlockHash: Hash,
        val merkleRootHash: Hash,
        val time: UnixTime
)