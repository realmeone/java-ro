package one.realme.chain

import one.realme.common.UnixTime

data class BlockHeader(
        val version: Int,
        val prevBlockHash: Hash,
        val merkleRootHash: Hash,
        val time: UnixTime
)