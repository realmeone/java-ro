package one.realme.krot.net.message

import com.google.protobuf.ByteString
import one.realme.krot.chain.Hash
import one.realme.krot.common.UnixTime
import one.realme.krot.crypto.encoding.Hex
import one.realme.krot.net.romtp.Messages
import org.junit.jupiter.api.Test

class ProtobufTest {

    @Test
    fun testEncode() {
        val mb = Messages.Block.newBuilder()
                .setHash(Int.MAX_VALUE)
                .setVersion(1)
                .setTimestamp(UnixTime.now().toInt())
                .setPreviousBlockHash(ByteString.copyFrom(Hash.empty().toByteArray()))
                .setMerkleRootHash(ByteString.copyFrom(Hash.empty().toByteArray()))
                .build()

        println(Hex.encode(mb.toByteArray()))
    }
}