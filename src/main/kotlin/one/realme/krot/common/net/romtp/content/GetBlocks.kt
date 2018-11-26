package one.realme.krot.common.net.romtp.content

import one.realme.krot.common.primitive.Hash

/**
 * Return an inv packet containing the list of blocks
 * starting right after the last known hash in the block locator object,
 * up to hash_stop or 500 blocks, whichever comes first.
 */
class GetBlocks(
        val hashCount: Int, //number of block locator hash entries
        val hashStop: Hash // hash of the last desired block; set to zero to get as many blocks as possible (500)
) {}