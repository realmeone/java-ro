package one.realme.krot.common.net.romtp.content

import one.realme.krot.common.primitive.Hash

class Disconnect(
        val nodeId: Hash, // 32 bytes, the node id
        val reason: Int // 4 bytes, reason code
)