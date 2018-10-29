package one.realme.krot.net.message

class Command(val code: Int) {
    companion object {
        fun ping(): Command = Command(0x1)
        fun pong(): Command = Command(0x2)
        fun time(): Command = Command(0x3)
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Command
        if (code != other.code) return false
        return true
    }

    override fun hashCode(): Int {
        return code
    }

    override fun toString(): String {
        return "Command(code=$code)"
    }
}