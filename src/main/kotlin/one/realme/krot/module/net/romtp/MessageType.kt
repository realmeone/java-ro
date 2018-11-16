package one.realme.krot.module.net.romtp

import one.realme.krot.common.lang.toByteArray

class MessageType private constructor(val code: Int) {

    companion object {
        val VERSION = MessageType(0x00)
        val PING = MessageType(0x02)
        val PONG = MessageType(0x03)
        val GET_TIME = MessageType(0x04)
        val TIME = MessageType(0x05)
        val INV = MessageType(0x06)
        val GET_DATA = MessageType(0x07)
        val GET_BLOCKS = MessageType(0x08)
        val GET_HEADERS = MessageType(0x09)
        val BLOCK = MessageType(0x0A)
        val TX = MessageType(0x0B)
        private val MAX = MessageType(0xFF) // just for range check

        fun isValid(cmd: Int): Boolean = cmd >= VERSION.code && cmd <= MAX.code
        fun ofCode(code: Int): MessageType {
            require(isValid(code)) {
                "not valid type"
            }
            return MessageType(code)
        }
    }

    fun toByteArray(): ByteArray = code.toByteArray()

    override fun toString(): String {
        return "${javaClass.simpleName}(code=$code)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MessageType

        if (code != other.code) return false

        return true
    }

    override fun hashCode(): Int {
        return code
    }

}