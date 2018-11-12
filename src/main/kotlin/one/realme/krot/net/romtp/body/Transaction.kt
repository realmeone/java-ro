package one.realme.krot.net.romtp.body

class Transaction(
        val from: ByteArray,
        val to: ByteArray,
        val amount: Long,
        val payload: ByteArray = ByteArray(0),
        val timestamp: Int
)