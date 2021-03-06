package one.realme.krot.common.primitive

import one.realme.krot.common.digest.Digests
import java.util.*


/**
 * Merkle Tree
 *
 *
 *           ABCDEEEE .......Merkle root
 *              /        \
 *      ABCD          EEEE
 *      /    \             /
 *   AB    CD       EE         .......E is paired with itself
 *  /  \   /  \     /
 * A  B  C  D  E              .........Transactions Hex Data
 *
 * if just one hash, it's the root
 */
object Merkle {

    fun merkleTreeRoot(hashList: List<Hash>): Hash = buildMerkleTree(hashList)[0]

    fun buildMerkleTree(hashList: List<Hash>): List<Hash> {
        when (hashList.size) {
            0 -> return listOf(Hash.empty())
            1 -> return hashList
        }

        var result = hashList
        while (result.size > 1) {
            val remainder = result.size % 2
            val tempHashList = ArrayList<Hash>()

            // note: this "while" is stupid
            // why kotlin not support "for(i = 0; i < result.size - 1; i +=2)"
            var i = 0
            while (i < result.size - 1) {
                tempHashList.add(
                        Hash.fromBytes(
                                Digests.sha256Twice(
                                        result[i].toByteArrayLE(),
                                        result[i + 1].toByteArrayLE()
                                ).reversedArray()
                        )
                )
                i += 2
            }

            if (remainder == 1) {
                tempHashList.add(
                        Hash.fromBytes(
                                Digests.sha256Twice(
                                        result[result.size - 1].toByteArrayLE(),   // do not use result.last() replace
                                        result[result.size - 1].toByteArrayLE()
                                ).reversedArray()
                        )
                )
            }
            result = tempHashList
        }

        return result// only root left, return it
    }
}