package one.realme.chain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test


/**
 * test merkle compute
 *
 * all test data from https://btc.com
 *
 * use early block data with little transactions
 */
class MerkleTest {

    private val blocks = listOf(
            // bitcoin "block height 78758"
            // only one transaction, merkle root equal to this transaction hash
            Pair(
                    Hash.fromString("54480d759ba444d02b286e4682dda8642dec19bc6f180977e6296a140f3c7ca2"),
                    listOf(Hash.fromString("54480d759ba444d02b286e4682dda8642dec19bc6f180977e6296a140f3c7ca2"))
            ),
            // bitcoin "block height 78756"
            // merkle root hash: 0fb4ca088606f5eb9cf3c01a91db0f41e7d59632f85ff16b55852f255a367164
            Pair(
                    Hash.fromString("0fb4ca088606f5eb9cf3c01a91db0f41e7d59632f85ff16b55852f255a367164"),
                    listOf(
                            Hash.fromString("0584e05b1212a1b7b0d8e54511992c33689250a1b1be3dc24197b79437a31e67"),
                            Hash.fromString("4aa0845f1c70080fa5ed70f6e4fe9f4923db82dce386e196d0eac13f6e505ede"))
            ),
            // bitcoin block height 78735
            // merkle root hash : 1c80658bbec8a036ebdf2f218254a2cba151a6ede94bda9b78907b654a501b67
            Pair(
                    Hash.fromString("1c80658bbec8a036ebdf2f218254a2cba151a6ede94bda9b78907b654a501b67"),
                    listOf(Hash.fromString("0c877b8ffb52ed21f36d6affc0c379d48bdcb18ec43213ac00620fed794a522c"),
                            Hash.fromString("04ebe55cbd35aacd76e619015bb5075a666b2af31fc9fb819facc1e7101068d9"),
                            Hash.fromString("9a546066228efaf85a72aaf5d4545ea3e62f37d66f9a9d1c0ab057693e3c3dde"))
            ),
            // https://btc.com/0000000000000000001c84afe48acb4c8569b2ef896af08a3ec8c4bb171426c9
            // block height 545684
            // merkle root bec3a5af2a46e68c533e7ddda5c4f054f9e71ee27bdf1d05df212e6dda581a37
            Pair(
                    Hash.fromString("df1eda06b36f1b213cf7ef3aa20e49f3897f127d549e27d97a0bb94d2b5ee924"),
                    listOf(
                            Hash.fromString("f4eba8b07648e294939c6f7445d2f99feae9c5c820a47b9457f4acf5415cdcd1"),
                            Hash.fromString("1651bd987763d3525e37646589366aab09a34308db5075c468523a72a62c80cf"),
                            Hash.fromString("95433b2eeb8d44e68b5da88fc3ef08361bc99946aac9bdb1821efd73c06bae43"),
                            Hash.fromString("e0295f5b5ccda9e32024b562380cdd519ec4c7a6ef1bfb7199b2d23a85df564f"),
                            Hash.fromString("7f76257726494d090263be5b8c9a5ebfad23c4381303e2769f12427a4560efdb"),
                            Hash.fromString("8620df5ec9fee8a6ee5bde2b80ca6b2568b22faa8bd972584d483851de07f109"),
                            Hash.fromString("b259c5a887caac0c81179136a64096f44a56bfdb3a0b493cc43d07c2b3062ce8"),
                            Hash.fromString("bf076229f4e0e6b67303c1886c0e3d9c6c0c73679b303cbb12f7e0226ef52dcc"),
                            Hash.fromString("61e1c145021c985b0096a16c22b15080c47265cbbb6cf4f27cb48774c4bdbd18"),
                            Hash.fromString("502b40e60c2bb6195f1ab065d785c45735d0a1c6e4a01327ce5f9250c2ba37c7"),
                            Hash.fromString("391c59ef96c86529d359f0633583ebf7011ebb3f8828ece751eff3fbefb2c676"),
                            Hash.fromString("15ae401fa0a29dfca726afeb210056c20fbc746cd37a6848f05ee3498aa44fd7"),
                            Hash.fromString("ec031642925876545b58cc46d8b7988b33ee95e935def6d033b1584467ae2780"),
                            Hash.fromString("54175e7b76a0f3dc807eddd8c40a1c5768e821308cf29008837a50ba7fe8969f"),
                            Hash.fromString("7bd6dc1012e62887e594c8391cb568d28fbeee61c33a1960701a8187abe73c6c"),
                            Hash.fromString("d09e68c65b348a66bcda7334ce1057f9c62549b5ec31e5f2d8cf9cb064c4e976"),
                            Hash.fromString("fd80336ddfece03bcd80005eb12661e97c8efd6fb34f70586ef2d2d2cb9447a1"),
                            Hash.fromString("fcac371426302fb82e60b5df69f4470f89ee3143b308a22e843f309f39bd41c3"),
                            Hash.fromString("cda0ddcaaab8f99d406d876e5983d4115f1d5a7070ef646fd448b22d124326d8"),
                            Hash.fromString("30cc7b609e6a7d402ae90bc313942ca840f3b258e8e86a516afe4a287e202c31"),
                            Hash.fromString("e2c2d51bcb16dd82846efb1f9a08fcf9424b99a397d9c4dc9cfce3ebcfc317d4"),
                            Hash.fromString("6ab6d762419f101b13d79724327d5febf9545039178e405cb089485ff3822548"),
                            Hash.fromString("8d149b94f4eb8614901b3fe6dcd5d74d036d22d8d4225382c509352e2568af26"),
                            Hash.fromString("74dae29aa986128154212ed8255494d8511e5372fe9139b356214b6e84204758"),
                            Hash.fromString("bce39c1315a8839b7ea9ecba07ff3e6c533d0f396495645f95b9d86ea3ce74a0"),
                            Hash.fromString("a5daa646dc6be3d7f0b38cbb7906d2c3a43f79780616cfdf71693582fef3bbb2"),
                            Hash.fromString("16cff007e053497566b9890f86185d5a36cb8b026f898360fe6b41248f523fee")
                    )
            )
    )

    private val fakeBlocks = listOf(
            // bitcoin "block height 78756"
            // merkle root hash: 0fb4ca088606f5eb9cf3c01a91db0f41e7d59632f85ff16b55852f255a367164
            // fake one of the transactions
            Pair(
                    Hash.fromString("0fb4ca088606f5eb9cf3c01a91db0f41e7d59632f85ff16b55852f255a367164"),
                    listOf(
                            Hash.fromString("0584e05b1212a1b7b0d8e54511992c33689250a1b1be3dc24197b79437a31e68"),
                            Hash.fromString("4aa0845f1c70080fa5ed70f6e4fe9f4923db82dce386e196d0eac13f6e505ede"))
            )
    )

    @Test
    fun testMerkleTree() {
        for (block in blocks)
            assertEquals(block.first, Merkle.merkleTreeRoot(block.second))
    }

    @Test
    fun testMerkleTreeFail() {
        for (block in fakeBlocks)
            assertNotEquals(block.first, Merkle.merkleTreeRoot(block.second))
    }
}