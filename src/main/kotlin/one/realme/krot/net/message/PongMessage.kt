package one.realme.krot.net.message

class PongMessage : Message(
        command = Command.pong(),
        payload = "pong".toByteArray()
)