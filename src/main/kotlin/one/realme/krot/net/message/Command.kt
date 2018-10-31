package one.realme.krot.net.message

import one.realme.krot.common.toBytesLE

class Command private constructor(val cmd: Int) {

    companion object {
        val HELLO = Command(0x00)
        val DISCONNECT = Command(0x01)
        val PING = Command(0x02)
        val PONG = Command(0x03)
        val GET_TIME = Command(0x04)
        val TIME_NOW = Command(0x05)

        private val MAX = Command(0xFF) // just for range check

        fun isValid(cmd: Int): Boolean = cmd >= HELLO.cmd && cmd <= MAX.cmd
        fun newCommand(cmd: Int): Command {
            require(isValid(cmd)) {
                "not valid command"
            }
            return Command(cmd)
        }
    }

    fun toBytesLE(): ByteArray = cmd.toBytesLE()

    override fun toString(): String {
        return "${javaClass.simpleName}(cmd=$cmd)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Command

        if (cmd != other.cmd) return false

        return true
    }

    override fun hashCode(): Int {
        return cmd
    }
}