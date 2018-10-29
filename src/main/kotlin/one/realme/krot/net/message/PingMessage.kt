package one.realme.krot.net.message

class PingMessage : Message(
        command = Command.ping(),
        payload = "ping".toByteArray()
)