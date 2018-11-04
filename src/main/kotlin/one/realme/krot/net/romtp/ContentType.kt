package one.realme.krot.net.romtp

import one.realme.krot.common.toBytes

class ContentType private constructor(val code: Int) {

    companion object {
        val BINARY = ContentType(0x00)
        val TEXT = ContentType(0x01)
        val PROTOBUF = ContentType(0x10)

        private val MAX = ContentType(0xFF) // just for range check

        fun isValid(cmd: Int): Boolean = cmd >= BINARY.code && cmd <= MAX.code
        
        fun ofCode(code: Int): ContentType {
            require(isValid(code)) {
                "not valid content-type"
            }
            return ContentType(code)
        }
    }

    fun toBytes(): ByteArray = code.toBytes()

    override fun toString(): String {
        return "${javaClass.simpleName}(code=$code)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContentType

        if (code != other.code) return false

        return true
    }

    override fun hashCode(): Int {
        return code
    }
}