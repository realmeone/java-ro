package one.realme.krot.module.net.message

import com.google.protobuf.ByteString
import one.realme.krot.common.primitive.Block
import one.realme.krot.common.primitive.Hash
import one.realme.krot.common.lang.UnixTime
import one.realme.krot.common.lang.measureTimeSeconds
import one.realme.krot.common.codec.Hex
import one.realme.krot.net.romtp.Messages
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.stream.IntStream
import java.util.zip.Deflater
import java.util.zip.Inflater

class ProtobufTest {

    @Test
    fun whoIsFasterDecode() {
        val round = 1000000
        println("Serializable decode round : $round")
        val block = Block()
        val bytes = ByteBuffer.allocate(104)
                .put(block.version.toByte()) // int 4 bytes
                .put(block.prevBlockHash.toByteArray()) // hash 32 bytes
                .put(block.merkleRootHash.toByteArray()) // hash 32 bytes
                .put(block.timestamp.toByteArray()) // time 4 bytes
                .put(block.hash.toByteArray())
                .array()
        val zlibBytes = bytes.zlib()
        val zlibTimes = measureTimeSeconds {
            IntStream.range(1, round).forEach {
                zlibBytes.unzlib()
            }
        }
        println("zlib use time: $zlibTimes s")


        val protoBytes = Messages.Block.newBuilder()
                .setVersion(block.version)
                .setPreviousBlockHash(ByteString.copyFrom(block.prevBlockHash.toByteArray()))
                .setMerkleRootHash(ByteString.copyFrom(block.merkleRootHash.toByteArray()))
                .setTimestamp(block.timestamp.toInt())
                .setHash(block.hash.toInt())
                .build().toByteArray()

        val protoTimes = measureTimeSeconds {
            IntStream.range(1, round).forEach {
                Messages.Block.parseFrom(protoBytes)
            }
        }
        println("protobuf use time: $protoTimes s")
    }

    @Test
    fun whoIsFasterEncode() {
        val round = 200000
        println("Serializable encode round : $round")

        val block = Block()
        val zlibTimes = measureTimeSeconds {
            IntStream.range(1, round).parallel().forEach {
                val bytes = ByteBuffer.allocate(104)
                        .put(block.version.toByte()) // int 4 bytes
                        .put(block.prevBlockHash.toByteArray()) // hash 32 bytes
                        .put(block.merkleRootHash.toByteArray()) // hash 32 bytes
                        .put(block.timestamp.toByteArray()) // time 4 bytes
                        .put(block.hash.toByteArray())
                        .array()
                bytes.zlib()
            }
        }
        println("zlib use time: $zlibTimes s")

        val protoTimes = measureTimeSeconds {
            IntStream.range(1, round).parallel().forEach {
                Messages.Block.newBuilder()
                        .setVersion(block.version)
                        .setPreviousBlockHash(ByteString.copyFrom(block.prevBlockHash.toByteArray()))
                        .setMerkleRootHash(ByteString.copyFrom(block.merkleRootHash.toByteArray()))
                        .setTimestamp(block.timestamp.toInt())
                        .setHash(block.hash.toInt())
                        .build().toByteArray()
            }
        }
        println("protobuf use time: $protoTimes s")
    }

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


    @Test
    fun testZlibAndUnzlib() {
        val bytes = "12345678".toByteArray()
        val cprssed = bytes.zlib()
        Assertions.assertArrayEquals(bytes, cprssed.unzlib())
    }


    private fun ByteArray.unzlib(): ByteArray {
        val data = this
        val inflater = Inflater()
        inflater.setInput(data)
        return ByteArrayOutputStream(data.size).use {
            val buffer = ByteArray(1024)
            while (!inflater.finished()) {
                val count = inflater.inflate(buffer)
                it.write(buffer, 0, count)
            }
            inflater.end()
            it.toByteArray()
        }
    }

    private fun ByteArray.zlib(): ByteArray {
        val data = this
        val deflater = Deflater()
        deflater.setInput(data)
        return ByteArrayOutputStream(data.size).use {
            val buffer = ByteArray(1024)
            deflater.finish()
            while (!deflater.finished()) {
                val count = deflater.deflate(buffer) // returns the generated code... index
                it.write(buffer, 0, count)
            }
            deflater.end()
            it.toByteArray()
        }
    }
}