package one.realme.krot.chain

import one.realme.krot.common.UnixTime

data class BlockHeader(
        val version: Int,
        val prevBlockHash: Hash,
        val merkleRootHash: Hash,
        val time: UnixTime
)